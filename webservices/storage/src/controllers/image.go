package controllers

import (
	"github.com/gin-gonic/gin"
	"github.com/google/uuid"
	"io"
	"storage/src/services"
)

func GetImage(c *gin.Context) {
	ctx := services.GetAppContext(c)
	mc := services.GetMinioClientContext(c)
	imgId := c.Param("fileId")
	buffer, getErr := services.GetImageObject(ctx, mc, imgId)
	if getErr != nil && getErr != io.EOF {
		c.JSON(400, gin.H{
			"error": getErr.Error(),
		})
	} else {
		c.Header("Content-Type", "image/png")
		c.Data(200, "image/png", buffer)
	}
}

func RemoveImg(c *gin.Context) {
	ctx := services.GetAppContext(c)
	mc := services.GetMinioClientContext(c)
	imgId := c.Param("fileId")

	stat, rmError := services.RemoveImageObject(*ctx, mc, imgId)
	if rmError != nil {
		var httpCode int
		if stat == services.ImageNotFound {
			httpCode = 404
		} else {
			httpCode = 500
		}
		c.JSON(httpCode, gin.H{
			"code":  httpCode,
			"error": rmError.Error(),
		})
	} else {
		c.JSON(200, gin.H{
			"message": "Success",
			"stat":    stat,
		})
	}
}

func UploadImg(c *gin.Context) {
	img, imgErr := c.FormFile("file")

	if imgErr != nil {
		c.Header("Content-Type", "application/json")
		c.JSON(400, gin.H{
			"error": imgErr.Error(),
		})
	}

	// Upload
	ctx := services.GetAppContext(c)
	mc := services.GetMinioClientContext(c)

	uid := uuid.New()
	file, fileOpenErr := img.Open()
	if fileOpenErr != nil {
		c.JSON(400, gin.H{
			"error": fileOpenErr.Error(),
		})
	}

	stat, info, upErr := services.UploadImageObject(*ctx, mc, uid.String(), file, &img.Size)
	if upErr != nil {
		c.JSON(400, gin.H{
			"error": upErr.Error(),
		})
	} else {
		c.JSON(200, gin.H{
			"info": info,
			"stat": stat,
		})
	}
}
