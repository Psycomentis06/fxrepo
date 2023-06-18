package services

import (
	"context"
	"github.com/minio/minio-go/v7"
)

const (
	BucketCreated            = 0
	BucketAlreadyExists      = 1
	BucketCreateUnknownError = 2
)

func CreateBucket(ctx context.Context, c *minio.Client, name string) (state int, err error) {
	err = c.MakeBucket(ctx, name, minio.MakeBucketOptions{})

	if err != nil {
		exists, errBucketExists := c.BucketExists(ctx, name)
		if errBucketExists == nil && exists {
			return BucketAlreadyExists, nil
		} else {
			return BucketCreateUnknownError, errBucketExists
		}
	} else {
		return BucketCreated, nil
	}
}
