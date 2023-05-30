package utils

import (
	"bufio"
	"testing"
)

func TestGetShadowFile(t *testing.T) {
	file := GetShadowFile()
	scanner := bufio.NewScanner(file)

	fileContent := "\n"
	for scanner.Scan() {
		fileContent += scanner.Text()
		fileContent += "\n"
	}
	t.Log(fileContent)
	if err := scanner.Err(); err != nil {
		t.Fatal(err)
	}
}

func TestGetUserShadowString(t *testing.T) {
	file := GetShadowFile()
	str := GetUserShadowString(file)
	_ = file.Close()
	t.Log(str)
}

func TestGetUserLocalPassword(t *testing.T) {
	file := GetShadowFile()
	obj := GetUserLocalPassword(file)
	_ = file.Close()
	t.Log(obj)
}
