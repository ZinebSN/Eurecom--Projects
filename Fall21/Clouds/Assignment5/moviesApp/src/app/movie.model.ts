export class Movie{
    id:number;
    title: string;
    year: number;
    genre: string;

    constructor(id:number, title: string,
        year:number,
        genre:string){
            this.id=id,
            this.title=title,
            this.year=year,
            this.genre=genre
    }
}