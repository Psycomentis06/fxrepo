package services

import (
	"github.com/gin-gonic/gin"
	"github.com/minio/minio-go/v7"
	"golang.org/x/net/context"
	"storage/src/config"
)

const (
	EnvConfigContextName = "env_config"
	AppContextName       = "app_context"
	MinioClientName      = "minio_client"
)

func GetEnvConfigContext(c *gin.Context) *config.EnvConfig {
	return c.MustGet(EnvConfigContextName).(*config.EnvConfig)
}

func SetEnvConfigContext(c *gin.Context, envConfig *config.EnvConfig) {
	c.Set(EnvConfigContextName, envConfig)
}

func GetAppContext(c *gin.Context) *context.Context {
	return c.MustGet(AppContextName).(*context.Context)
}

func SetAppContext(c *gin.Context, ctx *context.Context) {
	c.Set(AppContextName, ctx)
}

func GetMinioClientContext(c *gin.Context) *minio.Client {
	return c.MustGet(MinioClientName).(*minio.Client)
}

func SetMinioClientContext(c *gin.Context, client *minio.Client) {
	c.Set(MinioClientName, client)
}
