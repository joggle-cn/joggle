package main

import (
	"fmt"
	"runtime"
)

func main() {
	fmt.Printf("Hello, %s!\n", runtime.GOARCH)
}
