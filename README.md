# Barren Land Analysis

## Summary

Barren Land Analysis is a Java 8 application that outputs areas of all fertile regions in a 400m by 600m farm land. A portion of the farm land is barren and each barren region is rectangular represented by its bottom left and top right x and y coordinates.

## Tech Stack

* Java 8
* Gradle (Gradle wrapper is provided so no need to worry about the specific Gradle version)
* JUnit 4.12 (Will be pulled automatically by Gradle as a `testCompile` dependency)

## Setup

### Clone the repo on your local machine (Command Line)

`git clone git@github.com:dayalm/barren_land_analysis.git`

That's it! You don't need anything else (Other than Java 8 of course)

## Testing (Command Line)

1. From the root level of your local repo, `cd` into the "**BarrenLandAnalysis**" folder
2. Run the command `./gradlew test`
3. Open the file ""**build/reports/tests/test/index.html**" to see the unit test results

<pre><code>Dayals-MacBook-Pro:BarrenLandAnalysis dayal$ ./gradlew test
:compileJava UP-TO-DATE
:processResources NO-SOURCE
:classes UP-TO-DATE
:compileTestJava UP-TO-DATE
:processTestResources NO-SOURCE
:testClasses UP-TO-DATE
:test UP-TO-DATE

BUILD SUCCESSFUL

Total time: 0.822 secs</code></pre>

<img src="Unit_Test_Results.png" width="555px" height="400px"/>

## Running (Command Line)

1. From the root level of your local repo, `cd` into the "**BarrenLandAnalysis**" folder
2. Run the command `./gradlew run -PappArgs="['48 192 351 207', '48 392 351 407','120 52 135 547','260 52 275 547']"` to see the output for the given barren land rectangles. 
3. Change the input arguments to run the application with a different set of coordinates for barren land rectangles.

<pre><code>Dayals-MacBook-Pro:BarrenLandAnalysis dayal$ ./gradlew run -PappArgs="['48 192 351 207', '48 392 351 407','120 52 135 547','260 52 275 547']"
:compileJava UP-TO-DATE
:processResources NO-SOURCE
:classes UP-TO-DATE
:run
22816 192608

BUILD SUCCESSFUL

Total time: 1.128 secs</code></pre>

