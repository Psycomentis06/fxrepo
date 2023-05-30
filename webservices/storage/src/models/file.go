package models

type File struct {
	Id      int
	Ref     string
	User User
	Type    string
}

const (
	// IMAGE means that the file is treated as an image in terms of streaming and downloading. It applies for vectors and others
	IMAGE = "image"
	// VIDEO means the file is a video
	VIDEO = "video"
	// ATTACHMENT means the file is a compressed file. this type of files represent 3D Models, templates and others
	ATTACHMENT = "attachment"
	// AUDIO means audio
	AUDIO = "audio"
)
