import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ImagePostListModel, ImagePostModel} from "../models/image-post";
import {environment} from '../../environments/environment'
import {Page} from "../models/page";
import {BehaviorSubject, debounceTime, map, Observable, Observer, Subject, switchMap, tap} from "rxjs";
import {Router} from "@angular/router";
import {ResponseModel} from "../core/models/response";

interface ImagePostsFilter {
    page?: number,
    search?: string,
    size?: number,
    category?: number,
    tag?: string,
    nsfw?: boolean,
    ts?: boolean
}

@Injectable({
    providedIn: 'root'
})
export class ImagePostService {

    readonly CREATE_NEW_IMAGE_POST_ENDPOINT = environment.api.fx_main.v1 + '/post/image/new';
    readonly GET_IMAGE_POST_PAGE_ENDPOINT = environment.api.fx_main.v1 + '/post/image/list';
    readonly GET_IMAGE_POST_BY_SLUG_ENDPOINT = environment.api.fx_main.v1 + '/post/image/{slug}';
    private imagePostsFilterData: ImagePostsFilter
    private $imagePostsFilterSubject: BehaviorSubject<ImagePostsFilter>
    private _imagePostsFilterObservable: Observable<Page<ImagePostListModel[]>>

    constructor(private http: HttpClient, private router: Router) {
        this.imagePostsFilterData = {}
        this.$imagePostsFilterSubject = new BehaviorSubject<ImagePostsFilter>({})
        this._imagePostsFilterObservable = this.$imagePostsFilterSubject
            .pipe(
                debounceTime(500),
                tap(params => {
                    router.navigate([], {
                        queryParams: params,
                        replaceUrl: true,
                        queryParamsHandling: "merge"
                    })
                }),
                switchMap(params => this.getImagePostPage(params.page, params.size, params.search, params.ts, params.category, params.tag, params.nsfw))
            )
    }

    createImagePost(d: ImagePostModel) {
        return this.http.post<ImagePostModel>(this.CREATE_NEW_IMAGE_POST_ENDPOINT, d)
    }

    getImagePostPage(page?: number, size?: number, search?: string, ts?: boolean, category?: number, tag?: string, nsfw?: boolean) {
        let params: any = {}
        if (!page) page = 0
        params['page'] = page
        if (!size) size = 10
        params['size'] = size
        if (!search) search = ""
        params['search'] = search
        if (category) params['category'] = category
        if (tag) params['tag'] = tag
        if (ts !== undefined) params['ts'] = ts
        if (nsfw !== undefined) params['nsfw'] = nsfw
        return this.http.get<Page<ImagePostListModel[]>>(this.GET_IMAGE_POST_PAGE_ENDPOINT, {
            params
        }).pipe(
            tap(() => {
                this.router.navigate([], {queryParams: this.imagePostsFilterData})
            })
        )
    }

    get imagePostPageObservable() {
        return this._imagePostsFilterObservable
    }

    set updateImagePostPage(data: ImagePostsFilter) {
        if (data.category) this.imagePostsFilterData.category = data.category
        if (data.tag) this.imagePostsFilterData.tag = data.tag
        if (data.page) this.imagePostsFilterData.page = data.page
        if (data.search) this.imagePostsFilterData.search = data.search
        if (data.nsfw) this.imagePostsFilterData.nsfw = data.nsfw
        if (data.size) this.imagePostsFilterData.size = data.size
        if (data.ts) this.imagePostsFilterData.ts = data.ts

        if (data.page === 0) delete this.imagePostsFilterData.page;
        if (data.search && data.search.trim().length === 0) delete this.imagePostsFilterData.search;
        if (data.category && data.category <= 0) delete this.imagePostsFilterData.category
        if (data.tag && data.tag.trim().length === 0) delete this.imagePostsFilterData.tag
        this.$imagePostsFilterSubject.next(this.imagePostsFilterData)
    }

    getImagePostBySlug(slug: string) {
        return this.http.get<ResponseModel<ImagePostModel>>(this.GET_IMAGE_POST_BY_SLUG_ENDPOINT.replace("slug", slug))
    }
}
