package services

import (
	"github.com/minio/minio-go/v7"
	"github.com/minio/minio-go/v7/pkg/credentials"
	"log"
	"storage/src/config"
)

// NewS3Client create the s3 client.
func NewS3Client(ec *config.EnvConfig) *minio.Client {
	minioClient, err := minio.New(ec.S3Endpoint, &minio.Options{
		Creds:  credentials.NewStaticV4(ec.S3AccessKey, ec.S3SecretKey, ""),
		Secure: false,
	})

	if err != nil {
		log.Fatal(err)
	}

	return minioClient
}
