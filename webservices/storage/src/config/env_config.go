package config

import (
	"log"
	"os"
	"reflect"
	"strconv"
)

type EnvConfig struct {
	// Amazon S3 config
	S3AccessKey string
	S3SecretKey string
	S3Endpoint  string
	S3Ssl       bool

	// Service's config
	ServiceUsername string
	ServicePassword string

	// Kafka
	KafkaBootstrapServers string
	KafkaGroupId          string
	KafkaAutoOffsetReset  string
}

func GetEnvConfig() EnvConfig {
	config := EnvConfig{}
	config.S3AccessKey = os.Getenv("S3_ACCESS_KEY")
	config.S3SecretKey = os.Getenv("S3_SECRET_KEY")
	config.S3Endpoint = os.Getenv("S3_ENDPOINT")
	S3Ssl := os.Getenv("S3_SSL")
	if len(S3Ssl) == 0 {
		S3Ssl = "false"
	}
	S3SslB, S3sslErr := strconv.ParseBool(S3Ssl)
	if S3sslErr != nil {
		log.Panic(S3sslErr)
	}
	config.S3Ssl = S3SslB

	config.ServiceUsername = os.Getenv("USERNAME")
	config.ServicePassword = os.Getenv("PASSWORD")

	config.KafkaBootstrapServers = os.Getenv("KAFKA_BOOTSTRAP_SERVERS")
	config.KafkaGroupId = os.Getenv("KAFKA_GROUP_ID")
	config.KafkaAutoOffsetReset = os.Getenv("KAFKA_AUTO_OFFSET_RESET")

	ref := reflect.ValueOf(config)
	typeOfRef := ref.Type()
	for i := 0; i < ref.NumField(); i++ {
		val := ref.Field(i).String()
		t := ref.Field(i).Type().String()
		if t == "string" && len(val) == 0 {
			fn := typeOfRef.Field(i).Name
			log.Panic("EnvConfig error: Field " + fn + " is empty")
		}
	}
	return config
}
