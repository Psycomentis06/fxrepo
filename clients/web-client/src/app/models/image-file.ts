import {FileVariantModel} from "./file-variant";

export interface ImageFileModel {
  id: string
  variants: FileVariantModel[]
  accentColor?: string
  colorPalette?: string
  landscape?: boolean
}
