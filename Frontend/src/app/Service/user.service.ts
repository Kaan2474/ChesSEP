import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {User} from "../Modules/User";

@Injectable({
  providedIn: "root"
})
export class UserService{
  private userURL:string;
  constructor(private http:HttpClient,
  ) {this.userURL="http://localhost:8080/users";}

  public register(fd:any){
    return this.http.post(this.userURL+"/save", fd);
  }
  public login(user:User){
    return this.http.post<User>(this.userURL+"/login", user);
  }
  public checkCode(user:User){
    return this.http.post<User>(this.userURL+"/authenticate", user);
  }
  public logout(user:User){
    return this.http.post<User>(this.userURL+"/logout", user);
  }


}
