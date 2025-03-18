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

[Phase 2 Diagram](
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwBzKBACu2GAGI0wKgE8YAJRRakEsFEFIIaYwHcAFkjAdEqUgBaAD4WakoALhgAbQAFAHkyABUAXRgAej0VKAAdNABvLMpTAFsUABoYXCl3aBlKlBLgJAQAX0wKcNgQsLZxKKhbe18oAAoiqFKKquUJWqh6mEbmhABKDtZ2GB6BIVFxKSitFDAAVWzx7Kn13ZExSQlt0PUosgBRABk3uCSYCamYAAzXQlP7ZTC3fYPbY9Tp9FBRNB6BAIDbULY7eRQw4wECDQQoc6US7FYBlSrVOZ1G5Y+5SJ5qBRRACSADl3lZfv8ydNKfNFssWjA2Ul4mDaHCMaFIXSJFE8SgCcI9GBPCTJjyaXtZQyXsL2W9OeKNeSYMAVZ4khAANbofWis0WiG0g6PQKwzb9R2qq22tBo+Ew0JwyLey029AByhB+DIdBgKIAJgADMm8vlzT6I2h2ugZJodPpDEZoLxjjAPhA7G4jF4fH440Fg6xQ3FEqkMiopC40Onuaa+XV2iHus30V6EFWkGh1VMKbN+etJeIGTLXVFM5544QCUSxv3eQvqc7ta7dUzyJ9vlyrjz5zU6jAAGJWeIAWWNJ7uZ-dY-hUUcPQOGXVRMVPB55XxXxlVVWdNS-bE3WeC82Q5G9SVNTdfTtEUxU3BCdV-Xp2A3C1sP9ECYxHKID0qLDsyjboen8eMk1TGA8hgQpb0wsjs1zNB820XQDGMHQUDtSstH0Zha28XxMBYptelbPgrySN40nSLsJB7PJ6L9RiGRAqJJ2klVRgM9Al09UDgjXCC1BQBAThQGC1SstAtW-aEej1NSvg0sNyOfV8P3whz6SIkyYCRFEAwxD0ukRZFURHIMlITGAU2TTA8wLETi0GGQK2GGAAHEeUeOT60UxtmCSmgoCiaJyreDt0i0Hl9L4wz0pCGLkAcSqygkSzeushKVzAnycWOMB3PGrM-W8xDz1eK8fmNAEX3fMMOLQAB1AAJQ03mC7MYAAXi4zz2kit1GpI7aeSmlAQge0zhhGyQ4LKVbZRCPV3i+LaurKZk+EqOtfD3OcDoUBBQGtOG7xgcGUFZHlQr2jGDpOs6YBhwkeNUG7uIwlBWhgeIrARpGQBR0nrq4g97pdB5fxi6J8gxyHofkknKcqPJEeR1HTQxrGylacoADoFZSN6uZbZqYl5nl+aJwWJemUWGaZ4X0Z5aWqflxXGN-TK2LTAoACIMYkO2Wo1iGoe1+tdZFtAxcZr3jbKU3ZYVuWUgEoTC1EoxsD0KBsBc+AoNUH6PEFuqAgav9WwSZItIxnrlvQdMpZ5YdVYG2zIMVXwfqW8M-UqEv-re1cObmk5Fs8gGf2QjbQfQk1VF28KLXx06rHOzyWYzCacwIn8nq9A9lfstu5VxJPa7593if9-XxdJxuTfgh6gZQg0jW3gXPcP+mD6NpvVFwgOOFPivxwRF-IeV9+mqOTW+CW2CNbbK7Fi4APDgVIsxhzDOUnO4GAAApCA04KrYyML7a06d4zGVVi1WIpwOr51MLPdMmU4AQEnFAI+bsy5dF-s9AAVigtAtdPKVBAPVChVCaEoEhjZD+rdwLtwWhaOu5Fu6+V7pefuL0yg4xHqqMehMp7kzuvPXyi9P7LxAh9NeRwThbwAZIqQZ8+7Xi-jvHWt995+1vo-BRL9lET0sdPbe7NhESD0Z4mi2A5C+FxJQ6AAByR4y9T7BD1KcWIfBhBBTxnkd4vwr4e1hjYn2Bt-YOLyATFx283EQI0aYhhXo4ppVsirZKsVUpAJATlPKgkoFRx0MASwiBFSwGANgeOO4CCuFTrVTKuCqnRACu1TSGR1BAJipwjpowBHwm8bNdesy8DzKKV4yJF4xlBQAFSOImBssxMAdnnX2cPMMRytlRFOTAc5YUX5HJKZ-MpP9gjUWqfFEcVt6o200PlIAA
)

## TODO
- [ ] Logout
- [ ] Create Game
- [ ] List Games
- [ ] Play Game
  - draw board
  - draw board from perspective of player
- [ ] Observe Game
  - observe game from perspective of white team