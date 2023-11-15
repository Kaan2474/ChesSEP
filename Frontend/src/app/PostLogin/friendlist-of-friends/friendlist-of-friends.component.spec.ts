import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FriendlistOfFriendsComponent } from './friendlist-of-friends.component';

describe('FriendlistOfFriendsComponent', () => {
  let component: FriendlistOfFriendsComponent;
  let fixture: ComponentFixture<FriendlistOfFriendsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FriendlistOfFriendsComponent]
    });
    fixture = TestBed.createComponent(FriendlistOfFriendsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
