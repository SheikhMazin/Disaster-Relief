package edu.ucalgary.oop;

/**
 * RequirementService
 *
 * Service class responsible for managing cultural and religious requirement
 * options available in the system. Available requirement types and their
 * options are loaded at startup from a serialized file named
 * available_requirements.ser located in the resources' directory.
 * The file contains a serialized CulturalOptions object whose accommodations
 * field is a HashMap mapping requirement type strings to sets of valid options.
 *
 * This service validates requirement selections against the loaded options
 * before they are applied to a victim, ensuring only known types and options
 * are accepted.
 *
 * @author Sheikh Muhammad Mazin
 * @version 1.0
 * @since 2026-01-01
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class RequirementService {

    private final DataRepository repository;
    private HashMap<String, Set<String>> availableOptions;

    // =========================================================================
    //  Constructor
    // =========================================================================

    /**
     * Constructs a RequirementService backed by the given data repository.
     * {@link #loadAvailableRequirements(String)} must be called after
     * construction to populate the available options before use.
     *
     * @param repository non-null DataRepository for persisting requirements
     * @throws IllegalArgumentException if repository is null
     */
    public RequirementService(DataRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("repository cannot be null.");
        }
        this.repository       = repository;
        this.availableOptions = new HashMap<>();
    }

    // =========================================================================
    //  Load
    // =========================================================================

    /**
     * Loads the available cultural and religious requirement types and their
     * options from the serialized CulturalOptions file at the given path.
     * The program should call this once at startup. If the file cannot be
     * found or read, an error message is printed and the application exits.
     *
     * @param fileName path to the serialized available_requirements.ser file
     * @throws IllegalArgumentException if fileName is null or blank
     * @throws IOException              if the file cannot be read
     * @throws ClassNotFoundException   if the serialized class cannot be found
     */
    @SuppressWarnings("unchecked")
    public void loadAvailableRequirements(String fileName) throws IOException, ClassNotFoundException {
        if (fileName == null || fileName.trim().isEmpty()) {
            throw new IllegalArgumentException("fileName cannot be null or empty.");
        }

        try (FileInputStream fis = new FileInputStream(fileName);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object obj = ois.readObject();

            // CulturalOptions is a serialized class with a field:
            // private HashMap<String, Set<String>> accommodations
            // We use reflection to access it since CulturalOptions may not
            // be directly available as a compiled class in our package.
            java.lang.reflect.Field field = obj.getClass().getDeclaredField("accommodations");
            field.setAccessible(true);
            this.availableOptions = (HashMap<String, Set<String>>) field.get(obj);

        } catch (IOException e) {
            System.err.println("Error: could not read requirements file: " + fileName);
            throw e;
        } catch (ClassNotFoundException e) {
            System.err.println("Error: requirements file format not recognised: " + fileName);
            throw e;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IOException("Failed to read accommodations from requirements file.", e);
        }
    }

    // =========================================================================
    //  Query methods
    // =========================================================================

    /**
     * Returns an unmodifiable set of all available requirement type keys
     * (e.g., "dietary restrictions", "safe-space requirements").
     *
     * @return unmodifiable set of requirement type strings; empty if not yet loaded
     */
    public Set<String> getAvailableRequirementTypes() {
        return Collections.unmodifiableSet(availableOptions.keySet());
    }

    /**
     * Returns an unmodifiable set of valid options for the given requirement type
     * (e.g., for "dietary restrictions": {"halal", "kosher", "vegetarian"}).
     *
     * @param requirementType non-null, non-empty requirement type string
     * @return unmodifiable set of valid option strings, or an empty set if the
     *         type is not found
     * @throws IllegalArgumentException if requirementType is null or blank
     */
    public Set<String> getOptionsForType(String requirementType) {
        if (requirementType == null || requirementType.trim().isEmpty()) {
            throw new IllegalArgumentException("requirementType cannot be null or empty.");
        }
        Set<String> options = availableOptions.get(requirementType.trim());
        if (options == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(options);
    }

    /**
     * Returns whether the given option is a valid selection for the given
     * requirement type based on the loaded available options.
     *
     * @param requirementType non-null, non-empty requirement type string
     * @param selectedOption  non-null, non-empty option string to validate
     * @return true if the option is valid for the given type, false otherwise
     * @throws IllegalArgumentException if either argument is null or blank
     */
    public boolean isValidOption(String requirementType, String selectedOption) {
        if (requirementType == null || requirementType.trim().isEmpty()) {
            throw new IllegalArgumentException("requirementType cannot be null or empty.");
        }
        if (selectedOption == null || selectedOption.trim().isEmpty()) {
            throw new IllegalArgumentException("selectedOption cannot be null or empty.");
        }
        Set<String> options = availableOptions.get(requirementType.trim());
        if (options == null) {
            return false;
        }
        return options.contains(selectedOption.trim());
    }
}