[English](./README.md) | [简体中文](./README.zh-CN.md)

# wkhtmltopdf-invoker

![Java](https://img.shields.io/badge/Java-17-blue)
![License](https://img.shields.io/badge/License-Apache%202.0-blue)

**A component to programmatically invoke wkhtmltopdf.** A Java-based wrapper around the [wkhtmltopdf](https://wkhtmltopdf.org/) command-line tool (and its `wkhtmltoimage` sibling), converting HTML documents to PDF / images by driving the external binary as a subprocess.

**Navigation**

- [1. Project Overview](#1-project-overview)
- [2. Features & Status](#2-features--status)
- [3. Requirements & Compatibility](#3-requirements--compatibility)
- [4. Architecture & Modules](#4-architecture--modules)
- [5. Installation](#5-installation)
- [6. Quick Start](#6-quick-start)
- [7. Configuration](#7-configuration)
- [8. Core Usage / API](#8-core-usage--api)
- [9. Testing & Build](#9-testing--build)
- [10. Versioning & Branches](#10-versioning--branches)
- [11. Contributing & License](#11-contributing--license)

## 1. Project Overview

`wkhtmltopdf-invoker` lets Java applications render HTML to PDF (and HTML to images) by invoking the native `wkhtmltopdf` / `wkhtmltoimage` binaries. It builds the command line, runs the forked process, captures stdout/stderr and reports the exit code — in the spirit of the classic "invoker" pattern.

**What it is**

- A thin, typed facade over the `wkhtmltopdf` command-line interface: `Invoker` + `InvocationRequest` + `InvocationResult`.
- Both conversions in one artifact: PDF (`WkhtmlToPdfInvocationRequest`) and image (`WkhtmlToImageInvocationRequest`).
- Command-line builders (`WkhtmlToPdfCommandLineBuilder` / `WkhtmlToImageCommandLineBuilder`) that you can extend for custom options.
- The implementation is based on [jhonnymertz/java-wkhtmltopdf-wrapper](https://github.com/jhonnymertz/java-wkhtmltopdf-wrapper).

**What it is not**

- Not an embedded WebKit — rendering happens in the external `wkhtmltopdf` process, which **must be installed** (see Section 3).
- Not a PDF generation library on its own — it does not produce PDF content; it converts HTML/URL input into PDF via the external tool.
- Not a container with a bundled binary — no platform binaries are shipped with this artifact.

**Typical scenarios**

| Scenario | How this component helps |
|:---|:---|
| Batch-render HTML reports / invoices to PDF | `WkhtmlToPdfInvocationRequest` + `DefaultInvoker` |
| Snapshot a web page as an image | `WkhtmlToImageInvocationRequest` (with output directory, LRS file options) |
| Web service endpoint that returns generated PDFs | Programmatic invocation from a servlet/Spring handler |
| Fine control over process output | `InvokerLogger`, `InvocationOutputHandler` (stdout/stderr), working directory, environment inheritance |

## 2. Features & Status

| Capability | Status | Description |
|:---|:---|:---|
| HTML / URL to PDF | Stable | `WkhtmlToPdfInvocationRequest` with URL input, base directory, encoding, timeouts, recursion/filter controls |
| HTML / URL to image | Stable | `WkhtmlToImageInvocationRequest` with output directory, LRS options |
| Option passthrough | Stable | Arbitrary extra arguments via `setGoals(List<String>)` (e.g. `--page-size`, `--header-html`) |
| Process control | Stable | Working directory, environment inheritance, system properties, output/error handlers |
| wkhtmltopdf discovery | Stable | `wkhtmltopdf.home` system property, `WKHTMLTOPDF_HOME` environment variable, or the default installation on `PATH` |
| Output capture | Stable | `InvocationOutputHandler` / `InvokerLogger` with `PrintStream` and `System.out` implementations |
| Result model | Stable | `InvocationResult` exposes the process exit code and the execution exception (`CommandLineException`) |
| Extensible builders | Stable | `AbstractCommandLineBuilder` subclasses per tool for custom command-line construction |
| Full CLI reference in-repo | Stable | `wkhtmltopdf.txt` at the repository root (usage of wkhtmltopdf 0.12.4) |

## 3. Requirements & Compatibility

| Requirement | Version / Note |
|:---|:---|
| JDK | 17+ (baseline of the `feature/2.0.x` branch) |
| Maven | 3.0+ |
| **wkhtmltopdf binary** | **Must be installed and working on your system** — `wkhtmltopdf` (and `wkhtmltoimage` if used). The library locates it via the `wkhtmltopdf.home` system property, the `WKHTMLTOPDF_HOME` environment variable, or the default installation on `PATH` |
| X server (optional) | Headless Linux environments may require `xvfb`-style virtual display for some wkhtmltopdf builds |

**Version line matrix**

| Branch | JDK | Version pattern |
|:---|:---|:---|
| `feature/1.0.x` | 8 | `1.0.x.*` |
| `feature/2.0.x` | 17 | `2.0.x.*` |
| `feature/3.0.x` | 21 | `3.0.x.*` |

This document describes the `feature/2.0.x` line (current version: `2.0.x.x.20260630-SNAPSHOT`).

## 4. Architecture & Modules

```text
 Application
     |
     | Invoker (facade)       DefaultInvoker
     v                        * working directory
 InvocationRequest            * wkhtmltopdf home
     |                        (wkhtmltopdf.home / WKHTMLTOPDF_HOME)
     v
 CommandLineBuilder (WkhtmlToPdf / WkhtmlToImage)
     |  builds argv + environment + working dir
     v
 External process: wkhtmltopdf / wkhtmltoimage
     |
     +-- stdout -> InvocationOutputHandler (SystemOut/PrintStream)
     +-- stderr -> InvocationOutputHandler
     +-- exit   -> InvocationResult (exit code + execution exception)
```

**Module list**

| Module | Type | Responsibility |
|:---|:---|:---|
| `wkhtmltopdf-invoker` | Single jar (library) | Facade, request/result model, command-line builders, process I/O handling |

**Package layout** (`io.github.easy4j.wkhtmltopdf.invoker`)

| Package | Content |
|:---|:---|
| `invoker` (root) | `Invoker`, `DefaultInvoker`, `InvocationResult`, `DefaultInvocationResult`, `InvokerLogger`, `PrintStreamLogger`, `SystemOutLogger`, `InvocationOutputHandler`, `PrintStreamHandler`, `SystemOutHandler` |
| `request` | `InvocationRequest`, `AbstractInvocationRequest`, `WkhtmlToPdfInvocationRequest`, `DefaultWkhtmlToPdfInvocationRequest`, `WkhtmlToImageInvocationRequest`, `DefaultWkhtmlToImageInvocationRequest` |
| `command` | `AbstractCommandLineBuilder`, `WkhtmlToPdfCommandLineBuilder`, `WkhtmlToImageCommandLineBuilder` |
| `exception` | `CommandLineConfigurationException`, `WkhtmlToPdfInvocationException` |

## 5. Installation

> **Assumption**: artifacts are currently distributed through the project's private Maven repository (Aliyun) and GitHub Releases; the library is **not yet published to Maven Central**. If the coordinates below cannot be resolved, either add the private repository to your build or install locally with `./mvnw install`.

**Maven**

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>wkhtmltopdf-invoker</artifactId>
    <version>2.0.x.x.20260630-SNAPSHOT</version>
</dependency>
```

**Gradle**

```gradle
implementation 'io.github.easy4j:wkhtmltopdf-invoker:2.0.x.x.20260630-SNAPSHOT'
```

Remember to install the **wkhtmltopdf binary** itself (see Section 3).

## 6. Quick Start

```java
import io.github.easy4j.wkhtmltopdf.invoker.DefaultInvoker;
import io.github.easy4j.wkhtmltopdf.invoker.InvocationResult;
import io.github.easy4j.wkhtmltopdf.invoker.Invoker;
import io.github.easy4j.wkhtmltopdf.invoker.request.DefaultWkhtmlToPdfInvocationRequest;
import io.github.easy4j.wkhtmltopdf.invoker.request.WkhtmlToPdfInvocationRequest;

import java.io.File;

Invoker invoker = new DefaultInvoker();

WkhtmlToPdfInvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
request.setURL("https://www.example.com");        // or a local HTML file path
request.setOutputFile(new File("target/example.pdf"));
request.setEncoding("UTF-8");
request.setGrayscale(true);
request.setTimeout(60_000L);

InvocationResult result = invoker.execute(request);

if (result.getExecutionException() != null) {
    throw result.getExecutionException();
}
System.out.println("Exit code: " + result.getExitCode());
```

**Expected result**: `target/example.pdf` exists and contains the rendered page; `result.getExitCode()` is `0` on success (the `wkhtmltopdf` process exit code); `result.getExecutionException()` is `null` unless the process could not be started or configured. Requires `wkhtmltopdf` to be installed and on `PATH` (or configured via `WKHTMLTOPDF_HOME`).

## 7. Configuration

There is **no configuration file and no property prefix** — everything is configured at runtime through the `Invoker` and the request objects.

| Setting | Where | Example |
|:---|:---|:---|
| wkhtmltopdf home | `Invoker.setWkhtmltopdfHome(File)` / request `setWkhtmltopdfHome` | `new File("/usr/local/bin")`; falls back to `wkhtmltopdf.home` system property, `WKHTMLTOPDF_HOME`, then `PATH` |
| Working directory | `Invoker.setWorkingDirectory(File)` | directory for the forked process |
| Logging | `Invoker.setLogger(InvokerLogger)` | `SystemOutLogger` by default |
| stdout / stderr capture | `Invoker.setOutputHandler` / `setErrorHandler` | `SystemOutHandler` by default; `PrintStreamHandler` for custom streams |
| PDF request options | `DefaultWkhtmlToPdfInvocationRequest` | `setURL`, `setBaseDirectory`, `setEncoding`, `setDelay`, `setFilterRegexp`, `setMatchRegexp`, `setMaxFiles`, `setMaxRecursions`, `setTimeout`, `setDontDownloadStylesheets` |
| Image request options | `DefaultWkhtmlToImageInvocationRequest` | `setOutputDirectory`, `setLrs`, `setLrsFile` |
| Shared print options | `InvocationRequest` (fluent setters) | `setOutputFile`, `setGoals`, `setCollate`, `setCopies`, `setDpi`, `setImageDpi`, `setGrayscale`, `setCookieJar`, `setProperties`, `setShellEnvironmentInherited`, `setVerbose`, `setDebug` |

## 8. Core Usage / API

**HTML string to PDF file** — write the HTML to a temp file, then invoke:

```java
Path html = Files.writeString(Files.createTempFile("report", ".html"),
        "<html><head><meta charset=\"utf-8\"></head><h1>Hello wkhtmltopdf</h1></html>");

WkhtmlToPdfInvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
request.setURL(html.toString());
request.setOutputFile(new File("report.pdf"));
request.setGoals(Arrays.asList("--page-size", "A4", "--enable-local-file-access"));

InvocationResult result = new DefaultInvoker().execute(request);
```

**HTML to image**:

```java
WkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
request.setURL("https://www.example.com");
request.setOutputDirectory(new File("target/shots"));
request.setOutputFile(new File("target/shots/home.png"));

InvocationResult result = new DefaultInvoker().execute(request);
```

**Capturing output**:

```java
Invoker invoker = new DefaultInvoker();
invoker.setOutputHandler(new PrintStreamHandler(System.out));
invoker.setErrorHandler(new PrintStreamHandler(System.err));
invoker.setLogger(new PrintStreamLogger(System.err));
```

See `wkhtmltopdf.txt` at the repository root for the full command-line reference (wkhtmltopdf 0.12.4).

## 9. Testing & Build

```bash
./mvnw clean verify     # build + JaCoCo coverage report
./mvnw clean install    # install into the local repository
```

- **Tests**: the Surefire configuration skips test execution by default (`skipTests=true`) in this module — rendering tests require a working `wkhtmltopdf` installation. The test sources still contain reference/legacy implementations inherited from the upstream wrapper (older API surface); they are **not** part of the current public API.
- **Coverage gate**: the POM configures JaCoCo with a 90% line-coverage minimum at the `verify` phase (`haltOnFailure=false`).

## 10. Versioning & Branches

| Branch | JDK | Version pattern | Notes |
|:---|:---|:---|:---|
| `feature/1.0.x` | 8 | `1.0.x.*` | Current line |
| `feature/2.0.x` | 17 | `2.0.x.*` | Next generation line |
| `feature/3.0.x` | 21 | `3.0.x.*` | Latest line |

- Snapshot versions follow the `1.0.x.yyyyMMdd-SNAPSHOT` scheme; releases are tagged `v{version}` and published through the project's private repository and GitHub Releases.
- The `feature/2.0.x` line is the actively maintained JDK 8 line; upgrade to `feature/2.0.x` (JDK 17) or `feature/3.0.x` (JDK 21) for newer JDK baselines.

## 11. Contributing & License

Contributions are welcome — please open an issue or a pull request on GitHub.

This project is licensed under the **Apache License, Version 2.0**. See the [LICENSE](./LICENSE) file for details.

> Attribution: the wrapper design is based on [jhonnymertz/java-wkhtmltopdf-wrapper](https://github.com/jhonnymertz/java-wkhtmltopdf-wrapper).
