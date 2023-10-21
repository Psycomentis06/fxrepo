package services

import (
	"context"
	"encoding/json"
	"github.com/confluentinc/confluent-kafka-go/kafka"
	"github.com/minio/minio-go/v7"
	"log"
	"storage/src/config"
	"strings"
)

const (
	ImageTopic = "fx_repo_topics_image"
)

var topics = []string{ImageTopic}

type Actions string

const (
	ADD        Actions = "ADD"
	REMOVE     Actions = "REMOVE"
	UPDATE     Actions = "UPDATE"
	PROCESSING Actions = "PROCESSING"
)

type Targets string

const (
	PRE_PROCESSING Targets = "PRE_PROCESSING"
	STORAGE        Targets = "STORAGE"
	MAIN           Targets = "MAIN"
)

type Status string

const (
	DONE        Status = "DONE"
	CANCELED    Status = "CANCELED"
	FAILED      Status = "FAILED"
	IN_PROGRESS Status = "IN_PROGRESS"
)

type KafkaData struct {
	EventId   string      `json:"eventId"`
	EventTime string      `json:"eventTime"`
	Action    Actions     `json:"action"`
	Targets   []Targets   `json:"targets"`
	Status    Status      `json:"status"`
	Payload   interface{} `json:"payload"`
}

type ImageVariantData struct {
	ID       int    `json:"id"`
	Original bool   `json:"original"`
	Width    int    `json:"width"`
	Height   int    `json:"height"`
	Size     int    `json:"size"`
	Title    string `json:"title"`
	URL      string `json:"url"`
	MD5      string `json:"md5"`
	SHA256   string `json:"sha256"`
}

type ImageData struct {
	ID             string             `json:"id"`
	Placement      string             `json:"placement"`
	Orphan         bool               `json:"orphan"`
	Variants       []ImageVariantData `json:"variants"`
	AccentColor    string             `json:"accentColor"`
	ColorPalette   string             `json:"colorPalette"`
	Landscape      bool               `json:"landscape"`
	PerceptualHash string             `json:"perceptualHash"`
	DifferenceHash string             `json:"differenceHash"`
	ColorHash      string             `json:"colorHash"`
	AverageHash    string             `json:"averageHash"`
}

type ImagePostData struct {
	ID        string    `json:"id"`
	Slug      string    `json:"slug"`
	Title     string    `json:"title"`
	Content   string    `json:"content"`
	CreatedAt string    `json:"createdAt"`
	UpdatedAt string    `json:"updatedAt"`
	PostType  string    `json:"postType"`
	UserId    string    `json:"userId"`
	Publik    bool      `json:"publik"`
	Ready     bool      `json:"ready"`
	NSFW      bool      `json:"nsfw"`
	Tags      []Tag     `json:"tags"`
	Category  Category  `json:"category"`
	Thumbnail string    `json:"thumbnail"`
	Image     ImageData `json:"image"`
}

type Tag struct {
	Name string `json:"name"`
}

type Category struct {
	ID          int    `json:"id"`
	Name        string `json:"name"`
	Description string `json:"description"`
	SVGIcon     string `json:"svgIcon"`
	Thumbnail   string `json:"thumbnail"`
	BgColor     string `json:"bgColor"`
	FgColor     string `json:"fgColor"`
	Color       string `json:"color"`
}

func (c *ImagePostData) saveImagePost(ctx *context.Context, client *minio.Client) {
	// Remove thumbnail if exists
	if len(strings.Trim(c.Thumbnail, " ")) > 0 {
		_, getErr := GetImageObject(ctx, client, c.Thumbnail)
		if getErr == nil {
			_, err := RemoveImageObject(*ctx, client, c.Thumbnail)
			if err != nil {
				log.Println("Failed to remove thumbnail: ", err)
				return
			}
		}
	}
	// Remove variants except original.
	// If original image not saved. retrieve it from main service.
}

func KafkaConsumer(ctx *context.Context, config *config.EnvConfig, client *minio.Client) {
	consumer, err := kafka.NewConsumer(&kafka.ConfigMap{
		"bootstrap.servers": config.KafkaBootstrapServers,
		"group.id":          config.KafkaGroupId,
		"auto.offset.reset": config.KafkaAutoOffsetReset})
	if err != nil {
		return
	}
	defer consumer.Close()
	consumeErr := consumer.SubscribeTopics(topics, nil)
	if consumeErr != nil {
		return
	}
	for {
		event := consumer.Poll(1000)
		switch e := event.(type) {
		case *kafka.Message:
			if topic := *e.TopicPartition.Topic; topic == ImageTopic {
				message := string(e.Value)
				var kafkaData KafkaData
				jsonErr := json.NewDecoder(strings.NewReader(message)).Decode(&kafkaData)
				if jsonErr != nil {
					log.Print("Failed to decode json:", jsonErr)
					break
				}
				log.Println("ImageTopic event received:", kafkaData.EventId)
				for _, t := range kafkaData.Targets {
					if t == STORAGE {
						if kafkaData.Action == PROCESSING && kafkaData.Status == FAILED {
							log.Println("Image processing failed. Removing saved images")
							var imagePost ImagePostData
							marshal, err := json.Marshal(kafkaData.Payload)
							if err != nil {
								log.Println("Failed to marshal payload:", err)
								return
							}
							jsonErr2 := json.Unmarshal(marshal, &imagePost)
							if jsonErr2 != nil {
								log.Println("Failed to convert kafka payload to image post object", jsonErr2)
								return
							}
							imagePost.saveImagePost(ctx, client)
						}
					} else {
						log.Println("Not targeted to storage, skipping")
					}
				}
			} else {
				log.Println("UnknownTopic: ", topic)
			}
		case kafka.Error:
			log.Println("Error: ", e)
		}
	}
}
