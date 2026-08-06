# wkhtmltopdf-invoker

[English](./README.md) | [简体中文](./README.zh-CN.md)

[![Java](https://img.shields.io/badge/Java-8-orange)](https://github.com/easy-4-java/wkhtmltopdf-invoker) [![License](https://img.shields.io/badge/license-Apache%202.0-green)](./LICENSE)

A component to programmatically invoke wkhtmltopdf. —— 基于 Java 的 [wkhtmltopdf](https://wkhtmltopdf.org/) 命令行工具（及其姊妹命令 `wkhtmltoimage`）封装组件，通过驱动外部二进制子进程将 HTML 文档转换为 PDF / 图片。

## 目录

- [1. 项目概述](#1-项目概述)
- [2. 能力与状态](#2-能力与状态)
- [3. 环境要求与兼容性](#3-环境要求与兼容性)
- [4. 架构与模块](#4-架构与模块)
- [5. 安装](#5-安装)
- [6. 快速开始](#6-快速开始)
- [7. 配置](#7-配置)
- [8. 核心用法 / API](#8-核心用法--api)
- [9. 测试与构建](#9-测试与构建)
- [10. 版本线与分支](#10-版本线与分支)
- [11. 贡献与许可](#11-贡献与许可)

## 1. 项目概述

`wkhtmltopdf-invoker` 让 Java 应用通过调用原生 `wkhtmltopdf` / `wkhtmltoimage` 二进制实现 HTML 转 PDF（及 HTML 转图片）。它负责构造命令行、启动子进程、捕获 stdout/stderr 并报告退出码 —— 采用经典的 "invoker"（调用器）模式。

**它是什么**

- 对 `wkhtmltopdf` 命令行接口的轻量、类型化门面：`Invoker` + `InvocationRequest` + `InvocationResult`。
- 一个制品同时支持两种转换：PDF（`WkhtmlToPdfInvocationRequest`）与图片（`WkhtmlToImageInvocationRequest`）。
- 命令行构造器（`WkhtmlToPdfCommandLineBuilder` / `WkhtmlToImageCommandLineBuilder`），可继承扩展自定义选项。
- 实现基于 [jhonnymertz/java-wkhtmltopdf-wrapper](https://github.com/jhonnymertz/java-wkhtmltopdf-wrapper)。

**它不是什么**

- 不是内嵌 WebKit —— 渲染发生在外部 `wkhtmltopdf` 进程中，**必须安装**该二进制（见第 3 节）。
- 不是独立的 PDF 生成库 —— 它不产生 PDF 内容，而是通过外部工具将 HTML / URL 输入转换为 PDF。
- 不是自带二进制的容器 —— 本制品不随包分发任何平台二进制。

**典型场景**

| 场景 | 组件的作用 |
|:---|:---|
| 批量将 HTML 报表 / 发票渲染为 PDF | `WkhtmlToPdfInvocationRequest` + `DefaultInvoker` |
| 将网页截图保存为图片 | `WkhtmlToImageInvocationRequest`（支持输出目录、LRS 文件选项） |
| Web 服务端点返回生成的 PDF | 在 Servlet / Spring 处理器中程序化调用 |
| 精细控制进程输出 | `InvokerLogger`、`InvocationOutputHandler`（stdout/stderr）、工作目录、环境继承 |

## 2. 能力与状态

| 能力 | 状态 | 说明 |
|:---|:---|:---|
| HTML / URL 转 PDF | 稳定 | `WkhtmlToPdfInvocationRequest`：URL 输入、基础目录、编码、超时、递归 / 过滤控制 |
| HTML / URL 转图片 | 稳定 | `WkhtmlToImageInvocationRequest`：输出目录、LRS 选项 |
| 选项透传 | 稳定 | 通过 `setGoals(List<String>)` 传入任意附加参数（如 `--page-size`、`--header-html`） |
| 进程控制 | 稳定 | 工作目录、环境继承、系统属性、输出 / 错误处理器 |
| wkhtmltopdf 定位 | 稳定 | `wkhtmltopdf.home` 系统属性、`WKHTMLTOPDF_HOME` 环境变量，或 `PATH` 上的默认安装 |
| 输出捕获 | 稳定 | `InvocationOutputHandler` / `InvokerLogger`，提供 `PrintStream` 与 `System.out` 实现 |
| 结果模型 | 稳定 | `InvocationResult` 暴露进程退出码与执行异常（`CommandLineException`） |
| 可扩展构造器 | 稳定 | 每种工具对应一个 `AbstractCommandLineBuilder` 子类，便于自定义命令行构造 |
| 仓库内完整 CLI 参考 | 稳定 | 仓库根目录的 `wkhtmltopdf.txt`（wkhtmltopdf 0.12.4 用法说明） |

## 3. 环境要求与兼容性

| 要求 | 版本 / 说明 |
|:---|:---|
| JDK | 8+（`feature/1.0.x` 分支基线） |
| Maven | 3.0+ |
| **wkhtmltopdf 二进制** | **系统必须安装且可运行的 `wkhtmltopdf`**（使用图片转换时还需 `wkhtmltoimage`）。库按 `wkhtmltopdf.home` 系统属性、`WKHTMLTOPDF_HOME` 环境变量、或 `PATH` 上的默认安装顺序定位 |
| X 服务器（可选） | 无头 Linux 环境下，部分 wkhtmltopdf 构建需要 `xvfb` 类虚拟显示 |

**版本线矩阵**

| 分支 | JDK | 版本模式 |
|:---|:---|:---|
| `feature/1.0.x` | 8 | `1.0.x.*` |
| `feature/2.0.x` | 17 | `2.0.x.*` |
| `feature/3.0.x` | 21 | `3.0.x.*` |

本文档描述 `feature/1.0.x` 版本线（当前版本：`1.0.x.20260630-SNAPSHOT`）。

## 4. 架构与模块

```text
   应用
     |
     | Invoker（门面）          DefaultInvoker
     v                         * 工作目录
 InvocationRequest             * wkhtmltopdf 安装目录
     |                         （wkhtmltopdf.home / WKHTMLTOPDF_HOME）
     v
 CommandLineBuilder（WkhtmlToPdf / WkhtmlToImage）
     |  构造 argv + 环境变量 + 工作目录
     v
 外部进程：wkhtmltopdf / wkhtmltoimage
     |
     +-- stdout -> InvocationOutputHandler（SystemOut/PrintStream）
     +-- stderr -> InvocationOutputHandler
     +-- 退出码 -> InvocationResult（退出码 + 执行异常）
```

**模块清单**

| 模块 | 类型 | 职责 |
|:---|:---|:---|
| `wkhtmltopdf-invoker` | 单 jar（库） | 门面、请求 / 结果模型、命令行构造器、进程 IO 处理 |

**包结构**（`io.github.easy4j.wkhtmltopdf.invoker`）

| 包 | 内容 |
|:---|:---|
| `invoker`（根包） | `Invoker`、`DefaultInvoker`、`InvocationResult`、`DefaultInvocationResult`、`InvokerLogger`、`PrintStreamLogger`、`SystemOutLogger`、`InvocationOutputHandler`、`PrintStreamHandler`、`SystemOutHandler` |
| `request` | `InvocationRequest`、`AbstractInvocationRequest`、`WkhtmlToPdfInvocationRequest`、`DefaultWkhtmlToPdfInvocationRequest`、`WkhtmlToImageInvocationRequest`、`DefaultWkhtmlToImageInvocationRequest` |
| `command` | `AbstractCommandLineBuilder`、`WkhtmlToPdfCommandLineBuilder`、`WkhtmlToImageCommandLineBuilder` |
| `exception` | `CommandLineConfigurationException`、`WkhtmlToPdfInvocationException` |

## 5. 安装

> **假设**：制品目前通过项目私有 Maven 仓库（阿里云）与 GitHub Releases 分发；该库**尚未发布到 Maven Central**。若下列坐标无法解析，请在构建中配置私有仓库，或使用 `./mvnw install` 本地安装。

**Maven**

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>wkhtmltopdf-invoker</artifactId>
    <version>1.0.x.20260630-SNAPSHOT</version>
</dependency>
```

**Gradle**

```gradle
implementation 'io.github.easy4j:wkhtmltopdf-invoker:1.0.x.20260630-SNAPSHOT'
```

请记得另行安装 **wkhtmltopdf 二进制**（见第 3 节）。

## 6. 快速开始

```java
import io.github.easy4j.wkhtmltopdf.invoker.DefaultInvoker;
import io.github.easy4j.wkhtmltopdf.invoker.InvocationResult;
import io.github.easy4j.wkhtmltopdf.invoker.Invoker;
import io.github.easy4j.wkhtmltopdf.invoker.request.DefaultWkhtmlToPdfInvocationRequest;
import io.github.easy4j.wkhtmltopdf.invoker.request.WkhtmlToPdfInvocationRequest;

import java.io.File;

Invoker invoker = new DefaultInvoker();

WkhtmlToPdfInvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
request.setURL("https://www.example.com");        // 或本地 HTML 文件路径
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

**预期结果**：`target/example.pdf` 存在且包含渲染后的页面；成功时 `result.getExitCode()` 为 `0`（即 `wkhtmltopdf` 进程退出码）；除非进程无法启动或配置错误，否则 `result.getExecutionException()` 为 `null`。前提是系统已安装 `wkhtmltopdf` 且位于 `PATH`（或通过 `WKHTMLTOPDF_HOME` 配置）。

## 7. 配置

**没有配置文件、没有属性前缀** —— 一切均在运行时通过 `Invoker` 与请求对象配置。

| 设置项 | 位置 | 示例 |
|:---|:---|:---|
| wkhtmltopdf 安装目录 | `Invoker.setWkhtmltopdfHome(File)` / 请求 `setWkhtmltopdfHome` | `new File("/usr/local/bin")`；回退顺序：`wkhtmltopdf.home` 系统属性、`WKHTMLTOPDF_HOME`、`PATH` |
| 工作目录 | `Invoker.setWorkingDirectory(File)` | 子进程的工作目录 |
| 日志 | `Invoker.setLogger(InvokerLogger)` | 默认 `SystemOutLogger` |
| stdout / stderr 捕获 | `Invoker.setOutputHandler` / `setErrorHandler` | 默认 `SystemOutHandler`；自定义流使用 `PrintStreamHandler` |
| PDF 请求选项 | `DefaultWkhtmlToPdfInvocationRequest` | `setURL`、`setBaseDirectory`、`setEncoding`、`setDelay`、`setFilterRegexp`、`setMatchRegexp`、`setMaxFiles`、`setMaxRecursions`、`setTimeout`、`setDontDownloadStylesheets` |
| 图片请求选项 | `DefaultWkhtmlToImageInvocationRequest` | `setOutputDirectory`、`setLrs`、`setLrsFile` |
| 通用打印选项 | `InvocationRequest`（流式 setter） | `setOutputFile`、`setGoals`、`setCollate`、`setCopies`、`setDpi`、`setImageDpi`、`setGrayscale`、`setCookieJar`、`setProperties`、`setShellEnvironmentInherited`、`setVerbose`、`setDebug` |

## 8. 核心用法 / API

**HTML 字符串转 PDF 文件** —— 先将 HTML 写入临时文件，再调用：

```java
Path html = Files.writeString(Files.createTempFile("report", ".html"),
        "<html><head><meta charset=\"utf-8\"></head><h1>Hello wkhtmltopdf</h1></html>");

WkhtmlToPdfInvocationRequest request = new DefaultWkhtmlToPdfInvocationRequest();
request.setURL(html.toString());
request.setOutputFile(new File("report.pdf"));
request.setGoals(Arrays.asList("--page-size", "A4", "--enable-local-file-access"));

InvocationResult result = new DefaultInvoker().execute(request);
```

**HTML 转图片**：

```java
WkhtmlToImageInvocationRequest request = new DefaultWkhtmlToImageInvocationRequest();
request.setURL("https://www.example.com");
request.setOutputDirectory(new File("target/shots"));
request.setOutputFile(new File("target/shots/home.png"));

InvocationResult result = new DefaultInvoker().execute(request);
```

**捕获输出**：

```java
Invoker invoker = new DefaultInvoker();
invoker.setOutputHandler(new PrintStreamHandler(System.out));
invoker.setErrorHandler(new PrintStreamHandler(System.err));
invoker.setLogger(new PrintStreamLogger(System.err));
```

完整的命令行参考见仓库根目录的 `wkhtmltopdf.txt`（wkhtmltopdf 0.12.4）。

## 9. 测试与构建

```bash
./mvnw clean verify     # 构建 + JaCoCo 覆盖率报告
./mvnw clean install    # 安装到本地仓库
```

- **测试**：本模块的 Surefire 配置默认跳过测试执行（`skipTests=true`）—— 渲染测试需要可用的 `wkhtmltopdf` 安装。测试源码中仍保留继承自上游包装器的参考 / 遗留实现（旧 API 形态）；它们**不属于**当前公共 API。
- **覆盖率门禁**：POM 配置了 JaCoCo，在 `verify` 阶段校验行覆盖率不低于 90%（`haltOnFailure=false`）。

## 10. 版本线与分支

| 分支 | JDK | 版本模式 | 说明 |
|:---|:---|:---|:---|
| `feature/1.0.x` | 8 | `1.0.x.*` | 当前版本线 |
| `feature/2.0.x` | 17 | `2.0.x.*` | 下一代版本线 |
| `feature/3.0.x` | 21 | `3.0.x.*` | 最新版本线 |

- 快照版本遵循 `1.0.x.yyyyMMdd-SNAPSHOT` 命名；发布版本以 `v{version}` 打标签，并通过项目私有仓库与 GitHub Releases 分发。
- `feature/1.0.x` 是持续维护的 JDK 8 版本线；需要更新的 JDK 基线请升级到 `feature/2.0.x`（JDK 17）或 `feature/3.0.x`（JDK 21）。

## 11. 贡献与许可

欢迎贡献 —— 请在 GitHub 上提交 Issue 或 Pull Request。

本项目基于 **Apache License, Version 2.0** 许可发布。详见 [LICENSE](./LICENSE) 文件。

> 声明：包装器设计基于 [jhonnymertz/java-wkhtmltopdf-wrapper](https://github.com/jhonnymertz/java-wkhtmltopdf-wrapper)。
