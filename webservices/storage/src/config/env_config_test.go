package config

import (
	"log"
	"os"
	"testing"
)

func TestGetConfig(t *testing.T) {
	err := os.Setenv("S3_SSL", "1")
	if err != nil {
		log.Fatal(err)
	}

	err1 := os.Setenv("USERNAME", "user1234")
	if err1 != nil {
		log.Fatal(err1)
	}

	err2 := os.Setenv("S3_ACCESS_KEY", "sk")
	if err2 != nil {
		log.Fatal(err2)
	}
	GetEnvConfig()
}
