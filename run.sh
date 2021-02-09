#!/bin/bash
java -cp /app/app.jar:/app/libs/* -Dspring.profiles.active=docker accounting.AccountingSystem
