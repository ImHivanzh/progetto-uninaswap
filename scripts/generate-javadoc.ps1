$ErrorActionPreference = 'Stop'

$repoRoot = Resolve-Path (Join-Path $PSScriptRoot '..')
$srcDir = Join-Path $repoRoot 'src'
$outDir = Join-Path $repoRoot 'docs\javadoc'

$files = Get-ChildItem -Path $srcDir -Recurse -Filter *.java | ForEach-Object { $_.FullName }
if (-not $files) {
  throw "No .java files found in $srcDir"
}

$flatlafJar = Get-ChildItem -Path (Join-Path $repoRoot 'lib') -Filter 'flatlaf-*.jar' -ErrorAction SilentlyContinue |
  Sort-Object -Property Name -Descending |
  Select-Object -First 1

$javadocArgs = @(
  '-d', $outDir,
  '-locale', 'it_IT',
  '-encoding', 'UTF-8',
  '-charset', 'UTF-8',
  '-docencoding', 'UTF-8'
)
if ($flatlafJar) {
  $javadocArgs += @('-classpath', $flatlafJar.FullName)
}

$javadocArgs += $files

& javadoc @javadocArgs
