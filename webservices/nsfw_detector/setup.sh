#!/bin/bash

MODELS_PATH="./models"
NSFW_MODEL_PATH="$MODELS_PATH/nsfw.299x299.h5"
# Download NSFW prediction model

if [ ! -d $MODELS_PATH ]; then
    mkdir -p $MODELS_PATH
fi

if [ ! -f $NSFW_MODEL_PATH ]; then
    echo "NSFW model not found. Downloading..."
    wget -P $MODELS_PATH https://s3.amazonaws.com/ir_public/ai/nsfw_models/nsfw.299x299.h5
    echo "NSFW model downloaded."
  else
    echo "NSFW model found. Skipping download."
fi