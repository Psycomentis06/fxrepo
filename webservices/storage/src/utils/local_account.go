package utils

import (
	"github.com/GehirnInc/crypt"
	_ "github.com/GehirnInc/crypt/sha512_crypt"
	"log"
	"os/user"
)

func GetCurrentUser() *user.User {
	usr, err := user.Current()
	if err != nil {
		log.Panicf(err.Error())
	}
	return usr
}

/*
Algorithm ids https://manpages.debian.org/unstable/libcrypt-dev/crypt.5.en.html
*/
func CheckLocalPassword(password string) bool {
	file := GetShadowFile()
	localPass := GetUserLocalPassword(file)
	/*
			the Crypt package doesn't support all the C library's functions for now we will use the SHA512
			TODO: To do later
		var cryptObj crypt.Crypter
		switch localPass.HashAlgorithm {
		case "y":
			// yescrypt
		case "gy":
			// gost yesscript
		case "7":
			// scrypt
		case "2b":
			// bycrypt
		case "6":
			// sha512
		case "5":
			// sha256
		case "sha1":
			// sha1
		case "md5":
			// sunMD5
		case "1":
			// MD5
		}
	*/
	cryptObj := crypt.SHA512.New()
	_, err := cryptObj.Generate([]byte(password), []byte("$6$"+localPass.HashSalt))
	if err != nil {
		log.Fatalf(err.Error())
	}
	passCheckErr := cryptObj.Verify(localPass.ToString(), []byte(password))
	if passCheckErr == nil {
		return true
	}
	return false
}
