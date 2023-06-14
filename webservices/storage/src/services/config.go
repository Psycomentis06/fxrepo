package services

import (
	"github.com/gin-gonic/gin"
	"storage/src/config"
)

const (
	EnvConfigContextName = "env_config"
)

func GetEnvConfigContext(c *gin.Context) *config.EnvConfig {
	cnfPtr := c.MustGet(EnvConfigContextName)

	return cnfPtr.(*config.EnvConfig)
}

func SetEnvConfigContext(c *gin.Context, envConfig *config.EnvConfig) {
	c.Set(EnvConfigContextName, envConfig)
}
