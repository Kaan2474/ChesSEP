import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FriendProfilViewComponent } from './friend-profil-view.component';

describe('FriendProfilViewComponent', () => {
  let component: FriendProfilViewComponent;
  let fixture: ComponentFixture<FriendProfilViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FriendProfilViewComponent]
    });
    fixture = TestBed.createComponent(FriendProfilViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
