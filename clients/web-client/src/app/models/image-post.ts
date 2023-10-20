export interface ImagePostModel {
  title: string,
  content: string,
  public: boolean,
  nsfw: boolean,
  tags: string[],
  image: string,
  category: string
}

export interface ImagePostListModel {
  id: string,
  title: string,
  slug: string,
  thumbnail: string,
  content: string,
  tags: string[],
  image: string,
  category: string,
  nsfw: boolean
}
