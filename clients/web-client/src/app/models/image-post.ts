export interface ImagePostModel {
  title: string,
  content: string,
  public: boolean,
  nsfw: boolean,
  tags: string[],
  image: string,
  category: string
}
