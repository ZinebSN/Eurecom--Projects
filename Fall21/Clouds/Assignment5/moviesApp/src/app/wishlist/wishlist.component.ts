import { Component, OnInit } from '@angular/core';
import { Movie } from '../movie.model';
import { MoviesService } from '../movies.service';
import { User } from '../user.model';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { faHeartBroken } from '@fortawesome/free-solid-svg-icons';


@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent implements OnInit {
  faUser=faUser; 
  faHeartBroken=faHeartBroken;
  searchMovie:any;
  loader:boolean=true;
  user: User;
  movie:Movie;
  wishlist:Movie[]=[];
  notEmpty:boolean=false;

  constructor(public moviesService:MoviesService) { }

  ngOnInit(): void {
    this.user=this.moviesService.getUser();
    

    this.moviesService.getWishlist().subscribe(movies=>{ let m:Movie[]=[];m=movies;this.wishlist=m;this.notEmpty=(m.length!=0);this.loader=false});

  }

  removeFromWishlist(movie){
    this.moviesService.removeMovie(movie);
    this.wishlist=this.wishlist.filter(omovie => omovie!=movie);
  }
  

}
