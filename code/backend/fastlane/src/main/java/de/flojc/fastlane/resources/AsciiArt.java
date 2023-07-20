package de.flojc.fastlane.resources;

// The `AsciiArt` interface defines two constants `INIT_FASTLANE` and `READY`, which are both of type `String`. These
// constants represent ASCII art images. The `INIT_FASTLANE` constant represents an ASCII art image of the text "FASTLANE"
// and the `READY` constant represents an ASCII art image of the text "READY". These constants can be used to display these
// ASCII art images in a program.
public interface AsciiArt {

    String INIT_FASTLANE =
            """
                                                    
                    ██ ███    ██ ██ ████████     ███████ ███████ ██████  ██    ██ ██  ██████ ███████ ███████             
                    ██ ████   ██ ██    ██        ██      ██      ██   ██ ██    ██ ██ ██      ██      ██                  
                    ██ ██ ██  ██ ██    ██        ███████ █████   ██████  ██    ██ ██ ██      █████   ███████             
                    ██ ██  ██ ██ ██    ██             ██ ██      ██   ██  ██  ██  ██ ██      ██           ██             
                    ██ ██   ████ ██    ██        ███████ ███████ ██   ██   ████   ██  ██████ ███████ ███████     ██ ██ ██
                    """;

    String READY =
            """
                                                 
                    ██████  ███████  █████  ██████  ██    ██ ██
                    ██   ██ ██      ██   ██ ██   ██  ██  ██  ██
                    ██████  █████   ███████ ██   ██   ████   ██
                    ██   ██ ██      ██   ██ ██   ██    ██
                    ██   ██ ███████ ██   ██ ██████     ██    ██
                    """;
}
