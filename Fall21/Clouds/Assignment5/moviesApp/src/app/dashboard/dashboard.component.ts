import { Component, OnInit } from '@angular/core';
import { MoviesService } from '../movies.service';
import { User } from '../user.model';
import { Movie } from '../movie.model';
import '../app.module.ts';
import { faUser } from '@fortawesome/free-solid-svg-icons';



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  faUser=faUser;
  searchMovie:any;
  user: User;
  loader=true;
  movie:Movie;
  movies:Movie[]=[];
  

  constructor(public moviesService:MoviesService) {this.user=this.moviesService.getUser(); 
  }

  ngOnInit(): void {

    this.moviesService.getWishlist().subscribe(movies=>{ let m:number[]=[];m=movies.map(mo1=>mo1.id);
    this.moviesService.getMovies().subscribe(mo=>{this.movies=mo.filter(elt=>!m.includes(elt.id));this.loader=false})});
    
  }
 

  addMovieToWishlist(movie){
    this.moviesService.addMovie(movie);
    this.movies=this.movies.filter(omovie => omovie!=movie);
  }

}
