package ch.epfl.cs107.play.game.areagame;

import ch.epfl.cs107.play.game.Playable;
import ch.epfl.cs107.play.game.actor.Actor;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;

import java.util.LinkedList;
import java.util.List;


/**
 * Area is a "Part" of the AreaGame. It is characterized by its AreaBehavior and a List of Actors
 */
public abstract class Area implements Playable {

    // Context objects
    private Window window;
    private FileSystem fileSystem;
    // TODO implements me #PROJECT #TUTO
    /// List of actors inside the area
    private List<Actor> actors;
    private List<Actor> registeredActors;
    private List<Actor> unregisteredActors;
    // Camera Parameter
    // actor on which the view is centered
    private Actor viewCandidate;
    // effective center of the view
    private Vector viewCenter;
	/** @return (float): camera scale factor, assume it is the same in x and y direction */
    public abstract float getCameraScaleFactor();



    /**
     * Add an actor to the actors list
     * @param a (Actor): the actor to add, not null
     * @param forced (Boolean): if true, the method ends
     */
    private void addActor(Actor a, boolean forced) {
        if (a != null){
            // Here decisions at the area level to decide if an actor // must be added or not
            boolean errorOccured = !actors.add(a);
            if (errorOccured && !forced) {
                System.out.println("Actor " + a + " cannot be " +
                        "completely added, so remove it from where it was ");
                removeActor(a, true);
            }
        }else{
            System.out.println("Null actor cannot be added here");
        }
    }

    /**
     * Remove an actor form the actor list
     * @param a (Actor): the actor to remove, not null
     * @param forced (Boolean): if true, the method ends
     */
    private void removeActor(Actor a, boolean forced){
        if (a != null) {
            boolean errorOccured = !actors.remove(a);
            if (errorOccured && !forced) {
                System.out.println("Actor " + a + " cannot be " +
                        "completely removed , so add it to where it was ");
                addActor(a, true);
            }
        }
    }

    /**
     * Register an actor : will be added at next update
     * @param a (Actor): the actor to register, not null
     * @return (boolean): true if the actor is correctly registered
     */
    public final boolean registerActor(Actor a){
        // TODO implements me #PROJECT #TUTO

        if (a != null) {

            boolean errorOccured = !registeredActors.add(a);

            return !errorOccured;
        }
        return false;
    }

    /**
     * Unregister an actor : will be removed at next update
     * @param a (Actor): the actor to unregister, not null
     * @return (boolean): true if the actor is correctly unregistered
     */
    public final boolean unregisterActor(Actor a){
        // TODO implements me #PROJECT #TUTO
        if (a != null) {

            boolean errorOccured = !unregisteredActors.add(a);

            return !errorOccured;
        }

        return false;
    }

    /**
     * Getter for the area width
     * @return (int) : the width in number of cols
     */
    public final int getWidth(){
        // TODO implements me #PROJECT #TUTO
        return 0;
    }

    /**
     * Getter for the area height
     * @return (int) : the height in number of rows
     */
    public final int getHeight(){
        // TODO implements me #PROJECT #TUTO
        return 0;
    }

    /** @return the Window Keyboard for inputs */
    public final Keyboard getKeyboard () {
        // TODO implements me #PROJECT #TUTO
        return null;
    }

    /// Area implements Playable

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        // TODO implements me #PROJECT #TUTO
        actors = new LinkedList<>();
        registeredActors = new LinkedList<>();
        unregisteredActors = new LinkedList<>();
        this.fileSystem = fileSystem;
        this.window = window;
        viewCenter = Vector.ZERO;
        viewCandidate = null;
        return true;
    }

    /**
     * Resume method: Can be overridden
     * @param window (Window): display context, not null
     * @param fileSystem (FileSystem): given file system, not null
     * @return (boolean) : if the resume succeed, true by default
     */
    public boolean resume(Window window, FileSystem fileSystem){
        return true;
    }

    @Override
    public void update(float deltaTime) {
        purgeRegistration();
        for (Actor actor : actors) {
            actor.update(deltaTime);
        }
        updateCamera();
        for (Actor actor : actors) {
            actor.draw(window);
        }
    }


    private void updateCamera () {
        if(viewCandidate!=null){
            viewCenter=viewCandidate.getPosition();
        }
        Transform viewTransform = Transform.I.scaled(getCameraScaleFactor()).translated(viewCenter);
        window.setRelativeTransform(viewTransform);
    }

    /**
     * Suspend method: Can be overridden, called before resume other
     */
    public void suspend(){
        // Do nothing by default
        purgeRegistration();
    }


    @Override
    public void end() {
        // TODO save the AreaState somewhere
    }

    private final void purgeRegistration(){
        for (Actor actor: registeredActors) {
            addActor(actor,false);


        }
        for (Actor actor: unregisteredActors) {
            removeActor(actor, false);
        }
        registeredActors.clear();
        unregisteredActors.clear();
    }

    public final void setViewCandidate(Actor a){
        this.viewCandidate = a;
    }

}

