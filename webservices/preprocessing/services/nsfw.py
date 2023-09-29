import os

import nsfw_detector.predict


class NsfwDetector:
    def __init__(self, model_name):
        _root_dir_path = os.path.dirname(os.path.abspath(__file__))
        _model_path = os.path.join(_root_dir_path, '../models/' + model_name)
        self.model = nsfw_detector.predict.load_model(_model_path)

    def predict(self, img_path: str):
        return nsfw_detector.predict.classify(self.model, img_path, 299)
