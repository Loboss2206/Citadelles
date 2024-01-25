# Contributing to the Citadels of the team I

### Table of Contents

- [Code of Conduct](#code-of-conduct)
- [How to Contribute](#how-to-contribute)
    - [Create an Issue](#create-an-issue)
    - [Issue Lifecycle](#issue-lifecycle)
    - [Submit a Pull Request](#submit-a-pull-request)
- [Build from Source](#build-from-source)
- [Source Code Style](#source-code-style)
- [Reference Docs](#reference-docs)

### Code of Conduct

This project is governed by the [Team I CODE_OF_CONDUCT](CODE_OF_CONDUCT.md).

### How to Contribute

#### Create an Issue

Before creating a ticket, please take the time to check if it is not already created.

Once you're ready, create an issue
on [GitHub](https://github.com/pns-si3-projects/projet2-ps-23-24-citadels-2024-i/issues) with a little description of
the feature / fix / etc.

#### Issue Lifecycle

When an issue is created, any intersted person can assigned himself and setup a target
milestone.

When a fix is ready, the issue is closed and may still be re-opened until the fix is
released. After that the issue will typically no longer be reopened. In rare cases if the
issue was not at all fixed, the issue may be re-opened. In most cases however any
follow-up reports will need to be created as new issues with a fresh description.

#### Submit a Pull Request

1. Should you create an issue first? Yes, you should create issues BEFORE you can create the pull request.

1. Always check out the `dev` branch and submit pull requests against it.

1. Choose the granularity of your commits consciously and squash commits that represent
   multiple edits or corrections of the same logical change. See
   [Rewriting History section of Pro Git](https://git-scm.com/book/en/Git-Tools-Rewriting-History)
   for an overview of streamlining the commit history.

1. This project use [conventional commits](https://www.conventionalcommits.org). Please format your commit messages
   accordingly.
   Mark the issue fixed, e.g. `Closes #123` in the commit. See the
   [Commit Guidelines section of Pro Git](https://git-scm.com/book/en/Distributed-Git-Contributing-to-a-Project#Commit-Guidelines)
   for best practices around commit messages, and use `git log` to see some examples.

1. If there is a prior issue, reference the GitHub issue number in the description of the
   pull request.

If accepted, your contribution may be heavily modified as needed prior to merging.
You will likely retain author attribution for your Git commits granted that the bulk of
your changes remain intact. You may also be asked to rework the submission.

If asked to make corrections, simply push the changes against the same branch, and your
pull request will be updated. In other words, you do not need to create a new pull request
when asked to make changes.

### Build from Source

This document describes how to build the Citadels of the Team I from the command line. You may also be interested to see
Code Style.

Our project uses [Maven](https://maven.apache.org/download.cgi?.).

#### Before You Start

To build you will need `Git` and `JDK 17` in a location detected by `Maven`. Be sure that your JAVA_HOME environment
variable points to the jdk17 folder extracted from the JDK download.

#### Get the Source Code

```bash
git clone https://github.com/pns-si3-projects/projet2-ps-23-24-citadels-2024-i
cd spring-framework
```

#### Build from the Command Line

In order to build from the command line you must use a JDK 17 and run:

```bash
mvn clean install
```

If you just want to build the JAR file without running any tests:

```bash
mvn clean install -DskipTests
```

If you want to just run the tests without building a JAR file:

```bash
mvn test
```

If you want to run the java application:

```bash
mvn exec:java
```

### Source Code Style

#### Class declaration

Try as much as possible to put the implements, extends section of a class declaration on the same line as the class
itself.

Order the classes so that the most important comes first.

#### Naming

##### Constant names

Constant names use `CONSTANT_CASE`: all uppercase letters, with words separated by underscores.

Every constant is a `static final field`, but not all `static final` fields are constants. Constant case should
therefore be chosen only if the field is really a constant.

Example:

```java
// Constants
private static final Object TEST_OBJECT = new TestObject();
public static final int DEFAULT_PORT = -1;

// Not constants
private static final TestObject testObject = new TestObject();
private static final String testString = "Test String";
```

##### Variable names

Avoid using single characters as variable names. For instance prefer `Method method` to `Method m`.

#### Programming Practices

##### Null Checks

Use the asserts to check that a method argument is not null. Format the exception message so that the name of the
parameter comes first with its first character capitalized, followed by "must not be null". For instance

```java
public void handle(Event event) {
    assert event != null : "Event must not be null";
    //rest of the code
}
```

##### Use of @Override

Always add `@Override` on methods overriding or implementing a method declared in a super type.

##### Field and method references

A field of a class should always be referenced using `this`. A method of a class, however, should never be referenced
using `this`.

###### Local variable type inference

The use of `var` for variable declarations (local variable type inference) is not permitted. Instead, declare variables
using the concrete type or interface (where applicable).

##### Javadoc

###### Javadoc formatting

The following template summarizes typical Javadoc usage for a method.

```java
/**
 * Short description of the method.
 * <p>
 * Longer description of the method.
 * </p>
 * @param parameter1 Description of the first parameter. @Link{Type} can be used to link to a type.
 * @param parameter2 Description of the second parameter.
 * @return Description of the return value.
 * @throws ExceptionType Description of when this exception is thrown.
 */
public Method method(Type parameter1, Type parameter2) throws ExceptionType {
    //method body
}
```

In particular, please note:

Use an imperative style (i.e. Return and not Returns) for the first sentence.
No blank lines between the description and the parameter descriptions.
If the description is defined with multiple paragraphs, start each of them with <p>.
If a parameter description needs to be wrapped, do not indent subsequent lines.

##### Tests

Tests must be written using JUnit Jupiter (a.k.a., JUnit 5).

##### Naming

Each test class name must end with a Tests suffix.

### Reference Docs

The doc is available in a JavaDoc format in the `javadoc` folder. (It is updated at each release)
