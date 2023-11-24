import {FileVariantModel} from "./file-variant";

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

export interface ImagePostDetailsModel {
  title: string;
  updatedAt: string;
  thumbnail: string;
  category: {
    name: string;
    id: number;
  };
  nsfw: boolean;
  slug: string;
  createdAt: string;
  userId: string;
  id: string;
  content: string;
  image: {
    accentColor: string;
    averageHash: string;
    colorHash: string;
    perceptualHash: string;
    differenceHash: string;
    id: string;
    variants: FileVariantModel[];
  };
  tags: {
    name: string;
  }[];
}


