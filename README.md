# PagePilot

PagePilot is a small Java CLI that turns a book, a current page, and a deadline into a realistic day-by-day reading plan. You can exclude days when you do not want to read, and rerun the program later with your new current page to rebalance the remaining work.

## Features

- evenly distributes all remaining pages;
- supports custom days off;
- never leaves page gaps or overlaps;
- rebuilds the plan from current progress;
- uses only the Java standard library at runtime;
- includes unit tests for the planning logic.

## Requirements

- Java 17 or newer
- Maven 3.9 or newer

## Run

```bash
mvn clean package
java -jar target/pagepilot-1.0.0.jar
```

## Test

```bash
mvn test
```

## How it works

The planner gathers every available date from today through the deadline, removes selected days off, and divides the remaining pages across those dates. Any remainder is assigned one page at a time to the earliest reading days, keeping the daily load as balanced as possible.

## Project structure

```text
src/main/java/dev/pagepilot/   application and planning logic
src/test/java/dev/pagepilot/   unit tests
pom.xml                        Maven build configuration
```

## License

Released under the [MIT License](LICENSE).
