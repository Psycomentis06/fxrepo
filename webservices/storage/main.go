package main

import (
	"context"
	"github.com/gin-gonic/gin"
	"log"
	"storage/src/config"
	"storage/src/controllers"
	"storage/src/middlewares"
	"storage/src/services"
)

func main() {
	r := gin.New()
	cnf := config.GetEnvConfig()
	mc := services.NewS3Client(&cnf)
	ctx := context.Background()
	r.Use(gin.Logger(), gin.Recovery(), middlewares.InjectionMiddleware(&ctx, &cnf, mc))

	_, cbErr := services.CreateBucket(ctx, mc, config.Configuration.ImageBucketName)
	if cbErr != nil {
		log.Fatal(cbErr)
	} else {
		log.Print("Bucket Image created")
	}

	r.GET("/", func(c *gin.Context) {
		/*
			envCnf := services.GetEnvConfigContext(c)
			mc := services.NewS3Client(envCnf)
			ctx := context.Background()
		*/
		c.JSON(200, gin.H{
			"Message": "msg",
		})

	})
	apiV1Grp := r.Group("/api/v1")
	{
		imageGrp := apiV1Grp.Group("/image")
		{
			imageGrp.GET(":fileId", controllers.GetImage)
			imageGrp.POST("", middlewares.AuthorizationMiddleware(), controllers.UploadImg)
			imageGrp.DELETE(":fileId", middlewares.AuthorizationMiddleware(), controllers.RemoveImg)
		}
	}

	err := r.Run()
	if err != nil {
		log.Panicf(err.Error())
	} // listen and serve on 0.0.0.0:8080
}
