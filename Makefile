# This file makes my life easier in development, it allows me to
# not have to run multiple commands every few minutes since this
# cannot run in the Intellij terminal.

.ONESHELL: # Make should use 1 shell so directories work as intended
.DELETE_ON_ERROR: # Fail fast if a command fails
.PHONY: build # Forces the task to run

ROOT_DIR := $(PWD)

ARG_FILE := test/arguments.txt
ARGS := $(shell cat $(ARG_FILE))

all: build run

build:
	@echo "Building...."
	cd $(ROOT_DIR)
	./gradlew shadowJar

run: build
	java -jar ./build/libs/AsciiVideoPlayer-1.0.0-all.jar $(ARGS)