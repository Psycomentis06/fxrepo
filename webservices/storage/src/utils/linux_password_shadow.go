package utils

import (
	"bufio"
	"log"
	"os"
	"strings"
)

type UserLocalPassword struct {
	HashAlgorithm string
	HashSalt      string
	Hash          string
}

func GetShadowFile() *os.File {
	/*
		inside the docker container it will use the /etc/shadow but for now we are using
		an arbitrary file with similar "/etc/shadow" content
	*/
	// TODO: file, err := os.Open("/etc/shadow")
	user := GetCurrentUser()
	path := user.HomeDir + "/Desktop/shadow"
	file, err := os.Open(path)
	if err != nil {
		log.Panic(err.Error())
	} else {
		/*defer func(file *os.File) {
			err := file.Close()
			if err != nil {
				log.Println(err.Error())
			}
		}(file)*/
	}
	return file
}

func GetUserShadowString(file *os.File) string {
	currentUser := GetCurrentUser()
	scanner := bufio.NewScanner(file)

	for scanner.Scan() {
		if line := scanner.Text(); strings.HasPrefix(line, currentUser.Username) {
			return scanner.Text()
		}
	}

	if err := scanner.Err(); err != nil {
		log.Println(err)
	}
	log.Panic("User is not available in the default /etc/shadow file")
	return ""
}

func GetUserLocalPassword(file *os.File) UserLocalPassword {
	shadowString := GetUserShadowString(file)
	tokens := strings.Split(shadowString, ":")
	hashStr := tokens[1]
	hashSplit := strings.Split(hashStr, "$")
	return UserLocalPassword{hashSplit[1], hashSplit[2], hashSplit[3]}
}

func (l UserLocalPassword) ToString() string {
	return "$" + l.HashAlgorithm + "$" + l.HashSalt + "$" + l.Hash
}
