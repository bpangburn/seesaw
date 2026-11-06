# IDE setup and Maven builds

SeeSaw is a multi-module Maven project. **The root `pom.xml` is the entry point** for command-line builds and for importing into Eclipse (m2e) or NetBeans. Do not import individual child POMs first.

## Prerequisites and build expectations

- Use Maven 3.9.6 or a compatible version, subject to the parent POM's Enforcer rules.
- Use JDK 25 for development and Maven builds, unless the current parent POM specifies otherwise.
- The parent `pom.xml` configures `maven-compiler-plugin` with the **frgaal** compiler, `java-source=25` and `java-target=17`. Newer Java syntax can therefore be used while targeting Java 17 bytecode. Ordinary Eclipse JDT compilation is **not** frgaal compilation.
- `project.build.sourceEncoding=UTF-8` and the resource plugin's properties encoding are defined in Maven. A separate Eclipse project encoding preference is unnecessary.
- A Java 17 bytecode target **does not guarantee Java 17 runtime compatibility**. Source code or dependencies can still rely on newer Java APIs. Test deliverables on a Java 17 runtime and verify the runtime dependency tree before advertising Java 17 compatibility.
- For authoritative reproducible builds, run `mvn clean verify` in the repository root (or use the Maven Wrapper if installed).

## Fresh Eclipse import (m2e)

1. Clone the repository and start Eclipse with a JDK 25 installation available. Ensure Eclipse supports Java 25.
2. Select **File → Import → Maven → Existing Maven Projects**.
3. Choose the **repository root directory containing the parent `pom.xml`** (not the `pom.xml` file itself).
4. Select the root and discovered modules; finish the import. Eclipse/m2e generates local `.project`, `.classpath`, and some `.settings/` files.
5. Use **Maven → Update Project** if dependencies or POM settings change. Eclipse uses JDT for normal incremental compilation, while the Maven CLI uses frgaal. Eclipse source/compliance settings may need to be Java 25 so the IDE can parse newer syntax.
6. Run `mvn clean verify` from a terminal to verify the real Maven build.

**Reimporting a previously registered project:** If a project is closed and reports a missing `.project`, delete its *registration* from the Eclipse workspace via Project Explorer → Delete, **without selecting “Delete project contents on disk”**. Then repeat the Maven import. Git ignore rules do not prevent Eclipse from regenerating local files.

## NetBeans import

Open the repository's root `pom.xml` as a Maven project (or use **File → Open Project** on the repository root). NetBeans reads the Maven reactor and modules. Custom NetBeans run/debug/profile actions may be defined in the tracked `seesaw-demo/nbactions.xml`; those are not necessarily recreated by a fresh import. Use the Maven command line for authoritative build verification.

## Which IDE files are intentionally not tracked?

The root `.gitignore` applies recursively to Maven modules. Generic Eclipse project metadata (`.project`, `.classpath`), encoding and m2e preferences, empty NetBeans project metadata (`nbproject/`), and generated output (`target/`, `bin/`) are ignored. **Do not repeatedly delete these files locally while the IDE has the project open; Eclipse needs its local `.project`.**

### Customized IDE files deliberately retained in Git

These files include preferences or workflows that an IDE's one-time import will **not** reproduce exactly:

| File | Why retained | Future migration candidate |
| --- | --- | --- |
| `seesaw/.settings/org.eclipse.jdt.core.prefs` | Java 25 JDT compiler compliance and customized compiler warnings | Compare with Maven compiler configuration and shared static analysis; retain Java 25 for IDE syntax support |
| `seesaw-demo/.settings/org.eclipse.jdt.core.prefs` | Same Java 25/compiler warnings for demo | Same as above |
| `seesaw/.settings/org.eclipse.jdt.ui.prefs` | Customized `_swingset` cleanup settings | Export a shareable Eclipse cleanup/formatter profile if desired; not represented by Maven |
| `seesaw-demo/nbactions.xml` | Nondefault NetBeans run, debug and profile actions including `--enable-preview` | Evaluate replacing with documented Maven exec/debug commands |
| `seesaw/nb-configuration.xml` | Customized NetBeans hints and editor inspections | Evaluate whether to preserve IDE-specific hints or move overlapping checks to Checkstyle |
| `seesaw-demo/nb-configuration.xml` | Customized NetBeans hints and editor inspections | Same as above |

These files are **not added to `.gitignore` while tracked**. Ignoring an already tracked file would not stop its changes being committed.

### Additional candidates for future review (not deleted in this cleanup)

- The above six retained customized settings files, after verifying equivalent behavior can be restored after a clean IDE import.
- `seesaw-demo/nbactions.xml`: determine whether run/debug/profile behavior can be expressed through Maven configuration without changing developer workflows.
- All module-level `.gitignore` files: some now duplicate root-level patterns, but may contain module-specific exceptions such as `seesaw-demo/log/`; consolidate only after checking each file.
- Other IDE/editor helper files, if newly introduced (`.launch`, `.checkstyle`, formatter/exported preferences, IDE-specific configuration). **Inspect their content before ignoring them**; shared quality tooling may be worth retaining.
- `build/`, `dist/`, `*.jar`, and `*.log`: do **not** blanket-ignore or delete them without first verifying that they are generated rather than maintained assets.

## Maven versus Eclipse Java compilation

The parent POM intentionally has different source/target properties: frgaal handles Java 25 source with a Java 17 bytecode target, whereas Eclipse JDT uses Java 25 syntax/compliance in its customized tracked settings. Do not change the Maven target to 25 just to make Eclipse happy. The `java-release` property in the POM is also 25; check how each plugin uses it rather than assuming it overrides the explicit frgaal `source`/`target` configuration.
