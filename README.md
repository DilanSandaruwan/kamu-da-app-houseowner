# Kamu Da - House Owner App

##### Streamlining order and stock management to cater to customer requirements.

<img src="docs/houseownerapp_clip.gif" width="320">

## Overview

Welcome to **Kamu Da - House Owner App** – the ultimate tool for managing your food house
efficiently. This app empowers food house owners to handle orders, customize meals, and provide
seamless communication with customers. Stay organized with a real-time summary of orders and
prioritize tasks for a smooth operation.

### Key Features

- **Order Management**: View all incoming orders, including details and customer information. Access
  a comprehensive list of pending and accepted orders.

- **Order Details**: Dive into individual order details to understand specific customer preferences
  and requirements. Contact customers directly from the app.

- **Meal Customization**: Add new meals to your menu and customize food items based on customer
  preferences and dietary requirements.

- **Order Summary**: Get an overview of your orders, including the earliest accepted order and
  high-priority tasks. Stay organized to ensure timely order completion.

## Prerequisites

- IDE：Android Studio Flamingo | 2022.2.1 Patch 2
- Kotlin：1.6.21
- Java：8
- Gradle：8.0
- minSdk：24
- targetSdk：33

## Installation

1. Clone the repository.
   ```bash
   git clone https://github.com/DilanSandaruwan/kamu-da-app-houseowner.git

## Configuration

To configure URLs for your app, follow these steps:

1. Create a package, **`mysecret`** in the **`common/model/`** path.
2. Create a kotlin file **`MySecret.kt`** within the **`mysecret`** package.
3. Include host server URL. (this URL is addressed within `constant/NetworkConstant`)
4. Save the file.
5. Ensure that the **`mysecret`** package is listed in your `.gitignore` file to avoid accidentally
   exposing sensitive information.