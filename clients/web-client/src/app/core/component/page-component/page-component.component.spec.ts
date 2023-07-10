import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageComponentComponent } from './page-component.component';

describe('PageComponentComponent', () => {
  let component: PageComponentComponent;
  let fixture: ComponentFixture<PageComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PageComponentComponent]
    });
    fixture = TestBed.createComponent(PageComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
