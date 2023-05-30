package utils

import "testing"

func TestCheckLocalPassword(t *testing.T) {
	if validPass := CheckLocalPassword("392817654"); validPass {
		t.Log("Password is valid")
	} else {
		t.Error("Invalid password")
	}
}
