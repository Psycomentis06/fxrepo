from flask import Flask
from containers import Container
from views import image


def create_app() -> Flask:
    container = Container()
    container.config.redis.endpoint.from_env("REDIS_ENDPOINT")

    app = Flask(__name__)
    app.container = container
    app.add_url_rule('/', 'index', image.index)

    return app


if __name__ == '__main__':
    create_app().run(debug=True)
