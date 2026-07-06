v/ | /sys/ | /tmp/ | /usr/ | /var/ )
                # skip these directories
                ;;
            *)
                SEP="${SEP}${dir}"
                ;;
        esac
    done
    CYGPATH_ARGUMENT_LIST="$SEP"
    CYGPATH_OPTION="--path-option=absolute"
else
    CYGPATH_ARGUMENT_LIST=""
    CYGPATH_OPTION=""
fi

# Collect the arguments
CLASSPATH_OPTION="-classpath $CLASSPATH"

eval set -- $DEFAULT_JVM_OPTS $JAVA_OPTS $GRADLE_OPTS "\"-Dorg.gradle.appname=$APP_BASE_NAME\"" -classpath "\"$CLASSPATH\"" org.gradle.wrapper.GradleWrapperMain "$APP_ARGS"

# Escape application args
save () {
    for i do printf %s\\n "$i" | sed "s/'/'\\\\''/g;1s/^/'/;\$s/\$/' \\\\/" ; done
    echo " "
}
APP_ARGS=$(save "$@")

# Collect the arguments
exec "$JAVACMD" "$@"/sr
