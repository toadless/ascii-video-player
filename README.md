# Ascii Video Player
[![build](https://github.com/toadless/ascii-video-player/actions/workflows/build.yml/badge.svg)](https://github.com/toadless/ascii-video-player/actions/workflows/build.yml)

Converts a video/gif file into an ascii video and
streams the product to the console.

## FFmpeg
Ascii Video Player requires [ffmpeg](https://ffmpeg.org)
to create frames and audio. Please make sure that you have
it installed and added to path on your system.

You can check if you have ffmpeg installed and added to path
by running `ffmpeg` in your terminal.

## Running
Download the latest build at the 
[releases page](https://github.com/toadless/ascii-video-player/releases).
Then run the downloaded JAR file with `java -jar jarname.jar`. Make sure to
fill in `jarname` with the name of the downloaded jar and also add
your arguments after the initial command.

## Arguments

| Name  |      Description       | Alias | Required |    Type |
|:------|:----------------------:|:-----:|:--------:|--------:|
| input |     The Input File     |   i   |   true   |  String |
| char  | Character Aspect Ratio |   c   |   true   |  double |
| width | Width In Text To Print |   w   |   true   |     int |
| fps   |   Frames Per Second    |   f   |  false   |     int |
| audio |   Should Play Audio    |   a   |  false   | boolean |

**CAUTION** The player will error if audio is true and the
provided file has no audio inside it!

### Example Argument Array 1
` -i ./path/to/video -c 2.3 -w 110 -f 60 -audio true`

### Example Argument Array 2
` --input=./path/to/video --char=2.3 --width=110`

## Building
Ascii Video Player uses Gradle to handle dependencies and building! 
Make sure you have Java 8, and Git installed, and then run:
```bash
git clone https://github.com/toadless/ascii-video-player.git
cd ascii-video-player
./gradlew shadowJar
```
You should find your built JAR file located
in `build/libs`.

## License
Ascii Video Player is licensed under the [MIT License](https://opensource.org/licenses/MIT)
find out more [here](https://github.com/toadless/ascii-video-player/blob/main/LICENSE)
