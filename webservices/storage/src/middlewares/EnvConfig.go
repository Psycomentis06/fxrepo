package middlewares

import (
	"github.com/gin-gonic/gin"
	"storage/src/config"
	"storage/src/services"
)

func EnvConfigMiddleware(cnf *config.EnvConfig) gin.HandlerFunc {
	return func(c *gin.Context) {
		services.SetEnvConfigContext(c, cnf)
		c.Next()
	}
}
