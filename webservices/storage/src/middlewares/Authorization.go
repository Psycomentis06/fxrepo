package middlewares

import (
	"github.com/gin-gonic/gin"
	"log"
	"storage/src/services"
)

const (
	UsernameHeader = "X-FX-STORAGE-USERNAME"
	PasswordHeader = "X-FX-STORAGE-PASSWORD"
)

func AuthorizationMiddleware() gin.HandlerFunc {
	return func(c *gin.Context) {
		config := services.GetEnvConfigContext(c)
		eUsername := config.ServiceUsername
		ePassword := config.ServicePassword
		hUsername := c.GetHeader(UsernameHeader)
		hPassword := c.GetHeader(PasswordHeader)
		log.Print(hUsername + " " + hPassword)
		if eUsername != hUsername || ePassword != hPassword {
			c.AbortWithStatusJSON(401, gin.H{
				"code":    401,
				"message": "Unauthorized",
			})
			return
		}
		c.Next()
	}
}
