package middlewares

import (
	"context"
	"github.com/gin-gonic/gin"
	"github.com/minio/minio-go/v7"
	"storage/src/config"
	"storage/src/services"
)

func InjectionMiddleware(ctx *context.Context, cnf *config.EnvConfig, client *minio.Client) gin.HandlerFunc {
	return func(c *gin.Context) {
		services.SetEnvConfigContext(c, cnf)
		services.SetAppContext(c, ctx)
		services.SetMinioClientContext(c, client)
		c.Next()
	}
}
