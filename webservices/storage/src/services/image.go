package services

import (
	"context"
	"github.com/minio/minio-go/v7"
	"io"
	"storage/src/config"
)

const (
	ImageUploaded = 1 << iota
	ImageFound
	ImageNotFound
	ImageRemoved
	ImageUnknownError
)

func GetImageObject(ctx *context.Context, client *minio.Client, imgId string) (buffer []byte, err error) {
	obj, getErr := client.GetObject(*ctx, config.Configuration.ImageBucketName, imgId, minio.GetObjectOptions{})

	if getErr != nil {
		err = getErr
		return
	}
	defer obj.Close()
	stat, statErr := obj.Stat()

	if statErr != nil {
		err = statErr
		return
	}
	buffer = make([]byte, stat.Size)
	_, readErr := obj.Read(buffer)
	if readErr != nil {
		err = readErr
	}
	return buffer[:stat.Size], nil
}

func UploadImageObject(ctx context.Context, client *minio.Client, imgId string, reader io.Reader, size *int64) (state bool, info minio.UploadInfo, err error) {
	// Images will be received from the PreProcessing service in png format as a standard
	contentType := "image/png"
	uploadInfo, uploadErr := client.PutObject(ctx, config.Configuration.ImageBucketName, imgId, reader, *size, minio.PutObjectOptions{ContentType: contentType})

	if uploadErr != nil {
		err = uploadErr
		state = false
	} else {
		info = uploadInfo
		state = true
	}
	return
}

func RemoveImageObject(ctx context.Context, client *minio.Client, imgId string) (stat int8, err error) {
	_, statErr := client.StatObject(ctx, config.Configuration.ImageBucketName, imgId, minio.StatObjectOptions{})
	if statErr != nil {
		return ImageNotFound, statErr
	}
	rmErr := client.RemoveObject(ctx, config.Configuration.ImageBucketName, imgId, minio.RemoveObjectOptions{})

	if rmErr != nil {
		return ImageUnknownError, rmErr
	}
	return ImageRemoved, nil
}
