import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, ActivationStart, Router} from "@angular/router";
import {filter} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  constructor(private router: Router) {

  }

  ngOnInit() {
    this.router.events
      .pipe(filter(event => event instanceof ActivationStart))
      .subscribe(e => {
        const event = e as ActivationStart;
        const { title } = event.snapshot.data
        if (title) {
          document.title = title;
        } else {
          document.title = 'Fx Repo';
        }
      })
  }
}
