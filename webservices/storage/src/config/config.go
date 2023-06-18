package config

type Config struct {
	AppPrefix       string
	ImageBucketName string
}

var Configuration = &Config{
	AppPrefix:       "fx-repo",
	ImageBucketName: "fx-repo-image",
}
