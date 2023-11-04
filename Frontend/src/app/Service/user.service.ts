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

  public register(user:User){
    return this.http.post(this.userURL+"/register", user);
  }
  public login(user:User){
    return this.http.post<User>(this.userURL+"/authenticate", user);
  }
  public checkCode(user:User){
    return this.http.post<User>(this.userURL+"/twoFactor", user);
  }

}
