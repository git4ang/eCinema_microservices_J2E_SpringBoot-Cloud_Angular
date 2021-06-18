package ang.neggaw.movies.services.implementations;

import ang.neggaw.movies.entities.Category;
import ang.neggaw.movies.entities.Movie;
import ang.neggaw.movies.repositories.CategoryRepository;
import ang.neggaw.movies.repositories.MovieRepository;
import ang.neggaw.movies.services.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;

@Log4j2
@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {
    
    private final MovieRepository movieRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public Object createMovie(Movie m) {

        Movie movieDB = movieRepository.findByTitle(m.getTitle());
        if (movieDB != null) return String.format("Movie already exists with this title: '%s'.", movieDB.getTitle());

        Category categoryDB = categoryRepository.findByIdCategory(m.getIdCategory());
        if (categoryDB == null) return String.format("Unable to create Movie. Category with id: '%s' Not Found.", m.getIdCategory());

        m.setEntityState(Movie.EntityState.CREATED);
        m.setCategory(categoryDB);
        m.setReleaseDate(new Date());
        return movieRepository.save(m);
    }

    @Override
    public Movie getMovie(long idMovie) { return movieRepository.findByIdMovie(idMovie); }

    @Override
    public Collection<Movie> allMovies() {
        return movieRepository.findAll();
    }

    @Override
    public Object updateMovie(Movie m) {

        Movie movieDB = movieRepository.findByIdMovie(m.getIdMovie());
        if (movieDB == null) return String.format("Unable to update. Movie with id: '%s' Not Found.", m.getIdMovie());

        Category category = categoryRepository.findByIdCategory(m.getIdCategory());
        if (category == null) return String.format("Unable to update Movie. Category with id: '%s' Not Found.", m.getIdCategory());

        m.setEntityState(Movie.EntityState.UPDATED);
        m.setCategory(category);
        m.setProjections(movieDB.getProjections());
        return movieRepository.saveAndFlush(m);
    }

    @Override
    public Object deleteMovie(long idMovie) {

        Movie movieDB = movieRepository.findByIdMovie(idMovie);
        if (movieDB == null) return String.format("Unable to delete. Movie with id: '%s' Not Found.", idMovie);

        movieRepository.delete(movieDB);
        movieDB.setEntityState(Movie.EntityState.DELETED);

        return movieDB;
    }
}
