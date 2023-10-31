import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserProfilViewComponent } from './user-profil-view.component';

describe('UserProfilViewComponent', () => {
  let component: UserProfilViewComponent;
  let fixture: ComponentFixture<UserProfilViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserProfilViewComponent]
    });
    fixture = TestBed.createComponent(UserProfilViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
