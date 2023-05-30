package main

import (
	"github.com/gin-gonic/gin"
	"log"
)

func main() {
	r := gin.Default()
	r.GET("/api/v1/api_access/new", func(c *gin.Context) {
		c.JSON(200, gin.H{
			"message": "Generate an api access",
		})
	})
	err := r.Run()
	if err != nil {
		log.Panicf(err.Error())
	} // listen and serve on 0.0.0.0:8080
}
