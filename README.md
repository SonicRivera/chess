# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

[Phase 2 Diagram]([https://sequencediagram.org/index.html#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIei0azF5vDgHYsgwr5kvDrco4rGQiSEF8H5LCsACMvYAMwACxtv+v5FGqKDlAgB48rC+6Hqi6KxNiCaGC6YZumSFIGrS5ajsRcqkQS5EsuUICpCgIANlU+ivEssKQe8JpEuGFocgAsnIbEwK+KDvjxn6Os6THCRRnrekGAZBiGrrSpGHLRjAsbBtooYqbpsroeURkOjp5pJpell2gKJkkZ2KHlHhPLZrmmAoZ2P5XAMtGjAuqhgSAXzTrOzbjq2fTfleIKnNkPYwP2g69EFI4hbFfRhWg4E1kG0XzrlbZmJwq7eH4gReCg6B7gevjMMe6SZJgKUXmhJTlBU0gAKK7v19T9c0LQPqoT7dFFjboO2qHuaW07QEgABe8SJOUAA8M1zvkALnAWFlOvKMBYfYzW4U1voERi9HoRqzGkjA5JgCVml1rNaBCUybqieUnI1OEMC7Q1A1DSNhlaSZtkRsd4IwIqyqKYmyaHSWeHNd5CB5ujSUOT1pYJdQHb2V2ORgH2A5DgcFUrp41UbpCtq7tCMAAOKjqyrWnh157MMd17s8NY32KO03FV981sotPTLVAa0bWg22g2g+2+XjbIkZh0Kc6MqgfTOX33SdjEyE97pnbrXOGyVP1mhGhSWjAnIAJIAHLSCDktzqZv26U7HIux7Xti-rfsOzAW25OyZKjqzsQRyJZPay98fQijpFo0CJZhygCe4zn+PdQBfR56o4yVP0eeu9IlewYhTwnpkBoVhMXw6AgoANq3wHt08efu6O7d7DAjTE8cZOdZTaXU5l5eVxU1ejrX9fwQhTdtfqwX3H0Hdd5xvchf3UyD8Pe+j+PdNVeugTYD4UDYNw8C6pkHOjikW98xTWuJb1tQNFFuLYIPt0BDjPqMCepM-x4yuPLRWBBNpR1VvkaYPRT6jiHsfVYMA9gHSLr-RyalMh6xQLbL6swIEoBNkpc2ZlnrEJQKQ8hc5KGYLoknP6ekAYh29p9X2sN-ovy9G-HkHMFBiX6mQNOowsFiEEdHZ2VDOHmQJgjPOmc3KwJkWIPyKc-6ljLivOu5QG5ISgcXaeVMMrL1GKvUx69cHXwZrfAIlgUDKggMkGAAApCAYjSGBE7t3b+5hCGE2qJSO8LQ84S34WA3oT9gDuKgHACAWEoBsLsdICxMtYFyyDCtdaiDlbINAWrJ4SSUlpIySsAA6iwV2I0WgACFdwKDgAAaS+DXExMAzFOL0Wo+UAArfxaBmGq3fF3ap6ToBZJQLXGhiZlL+1JGMnkkzynTOSZQGp8ydFLJUXZQOPDPZ8KNgIi2Qi3bnICRIqROi5HHLhsMjCOjNFk0WhovRgs0w9FyVPfm1iabOLXDVAIXhkldi9LAYA2An6ECVp-Xm09wnXnBsNUarRjB9GltnVMVx8GpnCQjEA3A8CwmWWbWGrEKVwntsnU5mKRovP8gxBUSoVSuS+do8lsLsaFxJfokm-zAXJWBbPGxWUgI5QnJFcpC42zLkwEAA](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMm8vlzT6I2h2ugZJodPpDEZoLxjjAPhA7G4jF4fH440Fg6xQ3FEqkMiopC40Onuaa+XV2iHus30V6EFWkGh1VMKbN+etJeIGTLXVFM5544QCUSxv3eQvqc7ta7dUzyJ9vlyrjz5zU6jAAGJWeIAWWNJ7uZ-dY-hUUcPQOGXVRMVPB55XxXxlVVWdNS-bE3WeC82Q5G9SVNTdfTtEUxU3BCdV-Xp2A3C1sP9ECYxHKID0qLDsyjboen8eMk1TGA8hgQpb0wsjs1zNB820XQDGMHQUDtSstH0Zha28XxMBYptelbPgrySN40nSLsJB7PJ6L9RiGRAqJJ2klVRgM9Al09UDgjXCC1BQBAThQGC1SstAtW-aEej1NSvg0sNyOfV8P3whz6SIkyYCRFEAwxD0ukRZFURHIMlITGAU2TTA8wLETi0GGQK2GGAAHEeUeOT60UxtmCSmgoCiaJyreDt0i0Hl9L4wz0pCGLkAcSqygkSzeushKVzAnycWOMB3PGrM-W8xDz1eK8fmNAEX3fMMOLQAB1AAJQ03mC7MYAAXi4zz2kit1GpI7aeSmlAQge0zhhGyQ4LKVbZRCPV3i+LaurKZk+EqOtfD3OcDoUBBQGtOG7xgcGUFZHlQr2jGDpOs6YBhwkeNUG7uIwlBWhgeIrARpGQBR0nrq4g97pdB5fxi6J8gxyHofkknKcqPJEeR1HTQxrGylacoADoFZSN6uZbZqYl5nl+aJwWJemUWGaZ4X0Z5aWqflxXGN-TK2LTAoACIMYkO2Wo1iGoe1+tdZFtAxcZr3jbKU3ZYVuWUgEoTC1EoxsD0KBsBc+AoNUH6PEFuqAgav9WwSZItIxnrlvQdMpZ5YdVYG2zIMVXwfqW8M-UqEv-re1cObmk5Fs8gGf2QjbQfQk1VF28KLXx06rHOzyWYzCacwIn8nq9A9lfstu5VxJPa7593if9-XxdJxuTfgh6gZQg0jW3gXPcP+mD6NpvVFwgOOFPivxwRF-IeV9+mqOTW+CW2CNbbK7Fi4APDgVIsxhzDOUnO4GAAApCA04KrYyML7a06d4zGVVi1WIpwOr51MLPdMmU4AQEnFAI+bsy5dF-s9AAVigtAtdPKVBAPVChVCaEoEhjZD+rdwLtwWhaOu5Fu6+V7pefuL0yg4xHqqMehMp7kzuvPXyi9P7LxAh9NeRwThbwAZIqQZ8+7Xi-jvHWt995+1vo-BRL9lET0sdPbe7NhESD0Z4mi2A5C+FxJQ6AAByR4y9T7BD1KcWIfBhBBTxnkd4vwr4e1hjYn2Bt-YOLyATFx283EQI0aYhhXo4ppVsirZKsVUpAJATlPKgkoFRx0MASwiBFSwGANgeOO4CCuFTrVTKuCqnRACu1TSGR1BAJipwjpowBHwm8bNdesy8DzKKV4yJF4xlBQAFSOImBssxMAdnnX2cPMMRytlRFOTAc5YUX5HJKZ-MpP9gjUWqfFEcVt6o200PlIAA))
