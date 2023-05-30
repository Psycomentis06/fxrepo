package middlewares

import "github.com/gin-gonic/gin"

/*
	This middleware will check if the user super admin ( the super admin
	credentials are the username and password of the operating system)
*/

type formCredentials struct {
	username string
	password string
}

func SuperAdminAuthorization() gin.HandlerFunc {
	return func(c *gin.Context) {
		var form formCredentials
		err := c.Bind(&form)
		if err != nil {
			c.JSON(401, gin.H{
				"err": err.Error(),
			})
		}
		c.Next()
	}
}
